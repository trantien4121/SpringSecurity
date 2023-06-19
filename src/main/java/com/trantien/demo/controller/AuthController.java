package com.trantien.demo.controller;

import com.trantien.demo.exception.TokenRefreshException;
import com.trantien.demo.model.RefreshToken;
import com.trantien.demo.model.Role;
import com.trantien.demo.model.User;
import com.trantien.demo.payload.RoleEnum;
import com.trantien.demo.payload.request.LoginRequest;
import com.trantien.demo.payload.request.SignUpRequest;
import com.trantien.demo.payload.request.TokenRefreshRequest;
import com.trantien.demo.payload.response.JwtResponse;
import com.trantien.demo.payload.response.MessageResponse;
import com.trantien.demo.payload.response.TokenRefreshResponse;
import com.trantien.demo.repository.RoleRepository;
import com.trantien.demo.repository.UserRepository;
import com.trantien.demo.security.CustomUserDetail;
import com.trantien.demo.security.JwtUtils;
import com.trantien.demo.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signIn")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(customUserDetail);


        List<String> roles = customUserDetail.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .filter(authority -> authority.startsWith("ROLE_"))
                .collect(Collectors.toList());

        Set<String> permissions = customUserDetail.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .filter(authority -> !authority.startsWith("ROLE_"))
                .collect(Collectors.toSet());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(customUserDetail.getId());

        // Thiết lập cookie
        Cookie cookie = new Cookie("myRememberMeCookieName", jwt);
        cookie.setMaxAge(86400); // Thời gian sống của cookie (tính bằng giây), ở đây là 1 ngày
        cookie.setHttpOnly(true); // Chỉ cho trình duyệt sử dụng cookie, không cho script sử dụng
        cookie.setSecure(true); // Chỉ sử dụng cookie khi kết nối được mã hóa SSL / TLS (HTTPS)
        cookie.setPath("/"); // Đường dẫn truy cập cookie

        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken.getToken(),
                customUserDetail.getId(),
                customUserDetail.getUsername(),
                customUserDetail.getEmail(),
                roles,
                permissions));
    }
    @PostMapping("/signUp")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> asignRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (asignRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER.getRole())
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            if(asignRoles.size()==0) {
                throw  new RuntimeException("Error: Role is not found.");
            }
            asignRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN.getRole())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(RoleEnum.ROLE_MODERATOR.getRole())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER.getRole())
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        //Lay token(String) tu TokenRefreshRequest duoc gui len
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateJwtTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

}
