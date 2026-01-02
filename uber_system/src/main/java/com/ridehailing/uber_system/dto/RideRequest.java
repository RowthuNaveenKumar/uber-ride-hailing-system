package com.ridehailing.uber_system.dto;

import lombok.Data;

@Data
public class RideRequest {
    private double pickupLat;
    private double pickupLng;
    private double dropLat;
    private double dropLng;
    // getters & setters
}
