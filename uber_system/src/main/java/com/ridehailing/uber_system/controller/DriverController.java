package com.ridehailing.uber_system.controller;

import com.ridehailing.uber_system.dto.AuthResponse;
import com.ridehailing.uber_system.dto.DriverLoginRequest;
import com.ridehailing.uber_system.dto.DriverSignupRequest;
import com.ridehailing.uber_system.service.DriverService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody DriverSignupRequest request) {
        return driverService.signup(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody DriverLoginRequest request) {
        return driverService.login(request);
    }

    @PostMapping("/go-online")
    public String goOnline(Authentication authentication) {
        return driverService.goOnline(authentication.getName());
    }

    @PostMapping("/go-offline")
    public String goOffline(Authentication authentication) {
        return driverService.goOffline(authentication.getName());
    }
}
