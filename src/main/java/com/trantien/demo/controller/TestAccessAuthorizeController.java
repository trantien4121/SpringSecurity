package com.trantien.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/testAuthorize")
public class TestAccessAuthorizeController {
    @GetMapping("/all")
    public String allAccess(){
        return "Public Content!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess(){
        return "User Content";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String modAccess(){
        return "Moderator board";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminAccess(){
        return "Admin board";
    }

    @GetMapping("/getViewResources")
    @PreAuthorize("hasAuthority('VIEW')")
    public String viewResource(){
        return "This is resource that can View!";
    }
    @GetMapping("/getCreateResources")
    @PreAuthorize("hasAuthority('CREATE')")
    public String createResource(){
        return "This is resource that can Create!";
    }

    @GetMapping("/getUpdateResources")
    @PreAuthorize("hasAuthority('UPDATE')")
    public String updateResource(){
        return "This is resource that can Update!";
    }

    @GetMapping("/getDeleteResources")
    @PreAuthorize("hasAuthority('DELETE')")
    public String deleteResource(){
        return "This is resource that can DELETE!";
    }

}
