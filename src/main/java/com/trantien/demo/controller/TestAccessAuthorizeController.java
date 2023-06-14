package com.trantien.demo.controller;

import org.springframework.security.access.annotation.Secured;
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
    @PreAuthorize("hasAuthority('user') or hasAuthority('mod') or hasAuthority('admin')")
    public String userAccess(){
        return "User Content";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasAuthority('mod')")
    public String modAccess(){
        return "Moderator board";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('admin')")
    public String adminAccess(){
        return "Admin board";
    }

}
