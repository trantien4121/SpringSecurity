package com.trantien.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/testOauth2")
public class TestOauth2Controller {
    @GetMapping("/getPersonalResources")
    public String getPersonalResources() {
        return "Login with Oath2 successfully! This is Personal Resources";
    }
}
