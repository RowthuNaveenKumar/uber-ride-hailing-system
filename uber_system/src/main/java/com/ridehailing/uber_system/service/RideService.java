package com.ridehailing.uber_system.service;

import com.ridehailing.uber_system.dto.RideRequest;
import com.ridehailing.uber_system.dto.RideResponse;
import com.ridehailing.uber_system.model.Ride;
import org.springframework.transaction.annotation.Transactional;

public interface RideService {
    @Transactional
    RideResponse requestRide(RideRequest request,String customerEmail);
    Ride getAssignedRide(String driverEmail);
    @Transactional
    void startRide(Long rideId,String driverEmail);
    @Transactional
    void completeRide(Long rideId,String driverEmail);

    RideResponse viewRide(Long rideId, String customerEmail);

}
