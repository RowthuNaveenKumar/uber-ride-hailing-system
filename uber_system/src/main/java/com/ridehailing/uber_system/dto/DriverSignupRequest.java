package com.ridehailing.uber_system.dto;

import lombok.Data;

@Data
public class DriverSignupRequest {
    private String name;
    private String email;
    private String password;
    private String vehicleNumber;

    // getters & setters

}
