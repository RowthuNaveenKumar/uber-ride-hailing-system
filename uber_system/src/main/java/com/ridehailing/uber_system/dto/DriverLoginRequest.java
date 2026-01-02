package com.ridehailing.uber_system.dto;

import lombok.Data;

@Data
public class DriverLoginRequest {
    private String email;
    private String password;

    // getters & setters
}
