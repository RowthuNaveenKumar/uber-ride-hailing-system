package com.ridehailing.uber_system.service.impl;

import com.ridehailing.uber_system.dto.AuthResponse;
import com.ridehailing.uber_system.dto.LoginRequest;
import com.ridehailing.uber_system.dto.SignupRequest;
import com.ridehailing.uber_system.model.Role;
import com.ridehailing.uber_system.model.User;
import com.ridehailing.uber_system.repository.UserRepository;
import com.ridehailing.uber_system.service.AuthService;
import com.ridehailing.uber_system.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse signup(SignupRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already registered");
        }
        User user=new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));

        User savedUser = userRepository.save(user);

        String token=jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user=userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("Invalid credentials"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }
        String token=jwtService.generateToken(user.getEmail(),user.getRole().name());
        return new AuthResponse(token);
    }
}
