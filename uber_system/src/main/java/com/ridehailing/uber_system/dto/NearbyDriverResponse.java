package com.ridehailing.uber_system.dto;

import lombok.Data;

@Data
public class NearbyDriverResponse {
    private Long driverId;
    private String name;
    private double latitude;
    private double longitude;
    private double distanceKm;
}
