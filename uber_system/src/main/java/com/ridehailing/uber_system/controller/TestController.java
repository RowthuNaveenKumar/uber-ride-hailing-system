package com.ridehailing.uber_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/ping")
    public String ping(){
        return "Uber backend is running!";
    }
    @GetMapping("/secure")
    public String secure() {
        return "JWT is working!";
    }

}
