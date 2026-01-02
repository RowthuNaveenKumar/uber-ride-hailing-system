package com.ridehailing.uber_system.service;

import com.ridehailing.uber_system.dto.DriverLocationUpdateRequest;
import com.ridehailing.uber_system.dto.NearbyDriverResponse;

import java.util.List;

public interface DriverLocationService {
    String updateLocation(String email, DriverLocationUpdateRequest request);
    List<NearbyDriverResponse> findNearbyDrivers(double lat,double lng);
}
