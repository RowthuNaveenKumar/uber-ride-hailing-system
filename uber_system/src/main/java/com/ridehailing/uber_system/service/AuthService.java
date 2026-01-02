package com.ridehailing.uber_system.service;

import com.ridehailing.uber_system.dto.AuthResponse;
import com.ridehailing.uber_system.dto.LoginRequest;
import com.ridehailing.uber_system.dto.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);
    AuthResponse login(LoginRequest request);
}
