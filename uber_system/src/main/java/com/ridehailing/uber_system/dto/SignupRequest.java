package com.ridehailing.uber_system.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private String role; // CUSTOMER or DRIVER
}
