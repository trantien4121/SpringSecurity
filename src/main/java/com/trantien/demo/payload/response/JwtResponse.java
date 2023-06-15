package com.trantien.demo.payload.response;

import java.util.List;
import java.util.Set;

public class JwtResponse {
    private String token;
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private Set<String> permissions;

    public JwtResponse(String token, String refreshToken, Long id, String username, String email, List<String> roles, Set<String> permissions) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.permissions = permissions;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
