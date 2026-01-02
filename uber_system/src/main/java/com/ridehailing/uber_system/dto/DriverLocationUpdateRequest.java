package com.ridehailing.uber_system.dto;

import lombok.Data;

@Data
public class DriverLocationUpdateRequest {
    private double latitude;
    private double longitude;
}
