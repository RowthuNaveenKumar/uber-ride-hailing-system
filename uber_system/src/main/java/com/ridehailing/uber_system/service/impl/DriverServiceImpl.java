package com.ridehailing.uber_system.service.impl;

import com.ridehailing.uber_system.dto.AuthResponse;
import com.ridehailing.uber_system.dto.DriverLoginRequest;
import com.ridehailing.uber_system.dto.DriverSignupRequest;
import com.ridehailing.uber_system.model.Driver;
import com.ridehailing.uber_system.model.Role;
import com.ridehailing.uber_system.repository.DriverRepository;
import com.ridehailing.uber_system.service.DriverService;
import com.ridehailing.uber_system.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public DriverServiceImpl(DriverRepository driverRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse signup(DriverSignupRequest request) {
        if(driverRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Driver already exists");
        }
        Driver driver=new Driver();
        driver.setName(request.getName());
        driver.setEmail(request.getEmail());
        driver.setPassword(passwordEncoder.encode(request.getPassword()));
        driver.setVehicleNumber(request.getVehicleNumber());
        driver.setAvailable(false);
        driver.setRole(Role.DRIVER);

        driverRepository.save(driver);

        String token= jwtService.generateToken(driver.getEmail(),driver.getRole().name());
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(DriverLoginRequest request) {
        Driver driver = driverRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!passwordEncoder.matches(request.getPassword(), driver.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtService.generateToken(driver.getEmail(),driver.getRole().name());
        return new AuthResponse(token);
    }

    @Override
    public String goOnline(String email) {
        Driver driver = driverRepository.findByEmail(email).orElseThrow();
        driver.setAvailable(true);
        driverRepository.save(driver);
        return "Driver is ONLINE";
    }

    @Override
    public String goOffline(String email) {
        Driver driver = driverRepository.findByEmail(email).orElseThrow();
        driver.setAvailable(false);
        driverRepository.save(driver);
        return "Driver is OFFLINE";
    }
}
