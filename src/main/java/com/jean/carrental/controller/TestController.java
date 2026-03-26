package com.jean.carrental.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin/secret")
    public String adminSecret() {
        return "This is an ADMIN secret!";
    }

    @GetMapping("/user/info")
    public String userInfo() {
        return "This is an USER info endpoint!";
    }
}
