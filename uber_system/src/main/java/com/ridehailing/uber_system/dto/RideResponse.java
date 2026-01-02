package com.ridehailing.uber_system.dto;

import lombok.Data;

@Data
public class RideResponse {
    private Long rideId;
    private Long driverId;
    private String driverName;
    private String status;

    private String vehicleNumber;
    private Double driverLat;   // ⭐ for tracking
    private Double driverLng;   // ⭐ for tracking

    private Double pickupLat;
    private Double pickupLng;
    private Double dropLat;
    private Double dropLng;

    // getters & setters
}
