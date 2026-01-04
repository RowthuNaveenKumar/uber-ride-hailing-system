package com.ridehailing.uber_system.service;

import com.ridehailing.uber_system.dto.DriverLocationUpdateRequest;
import com.ridehailing.uber_system.dto.NearbyDriverResponse;
import com.ridehailing.uber_system.model.DriverLocation;

import java.util.List;

public interface DriverLocationService {
    String updateLocation(String email, DriverLocationUpdateRequest request);

    List<NearbyDriverResponse> findNearbyDrivers(double lat, double lng);

    DriverLocation getDriverLocationForRide(Long rideId, String customerEmail);
}
