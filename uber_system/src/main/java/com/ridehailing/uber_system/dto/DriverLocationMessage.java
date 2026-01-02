package com.ridehailing.uber_system.dto;

import lombok.Data;

@Data
public class DriverLocationMessage {
    private String token;   // 👈 ADD JWT
    private Long driverId;
    private Double latitude;
    private Double longitude;
}
