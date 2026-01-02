package com.ridehailing.uber_system.service;

import com.ridehailing.uber_system.dto.AuthResponse;
import com.ridehailing.uber_system.dto.DriverLoginRequest;
import com.ridehailing.uber_system.dto.DriverSignupRequest;

public interface DriverService {
    AuthResponse signup(DriverSignupRequest request);
    AuthResponse login(DriverLoginRequest request);

    String goOnline(String email);
    String goOffline(String email);
}
