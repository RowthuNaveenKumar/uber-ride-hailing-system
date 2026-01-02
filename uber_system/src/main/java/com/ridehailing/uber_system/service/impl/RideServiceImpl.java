package com.ridehailing.uber_system.service.impl;

import com.ridehailing.uber_system.dto.RideRequest;
import com.ridehailing.uber_system.dto.RideResponse;
import com.ridehailing.uber_system.model.Driver;
import com.ridehailing.uber_system.model.Ride;
import com.ridehailing.uber_system.model.RideStatus;
import com.ridehailing.uber_system.model.User;
import com.ridehailing.uber_system.repository.DriverLocationRepository;
import com.ridehailing.uber_system.repository.DriverRepository;
import com.ridehailing.uber_system.repository.RideRepository;
import com.ridehailing.uber_system.repository.UserRepository;
import com.ridehailing.uber_system.service.DriverLocationService;
import com.ridehailing.uber_system.service.RideService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RideServiceImpl implements RideService {
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final DriverLocationService locationService;
    private final UserRepository userRepository;

    private final DriverLocationRepository driverLocationRepository;

    public RideServiceImpl(RideRepository rideRepository, DriverRepository driverRepository, DriverLocationService locationService, UserRepository userRepository, DriverLocationRepository driverLocationRepository) {
        this.rideRepository = rideRepository;
        this.driverRepository = driverRepository;
        this.locationService = locationService;
        this.userRepository = userRepository;
        this.driverLocationRepository = driverLocationRepository;
    }

    @Override
    public RideResponse requestRide(RideRequest request, String customerEmail) {
        User customer=userRepository.findByEmail(customerEmail)
                .orElseThrow(()->new RuntimeException("Customer not found"));

        var nearbyDrivers=locationService.findNearbyDrivers(
                request.getPickupLat(), request.getPickupLng());

        if(nearbyDrivers.isEmpty()){
            throw new RuntimeException("No drivers available");
        }

        var selected=nearbyDrivers.get(0);

        Driver driver=driverRepository.findById(selected.getDriverId())
                .orElseThrow();

        driver.setAvailable(false);
        driverRepository.save(driver);

        Ride ride=new Ride();
        ride.setCustomer(customer);
        ride.setDriver(driver);
        ride.setPickupLat(request.getPickupLat());
        ride.setPickupLng(request.getPickupLng());
        ride.setDropLat(request.getDropLat());
        ride.setDropLng(request.getDropLng());
        ride.setStatus(RideStatus.ASSIGNED);
        ride.setRequestedAt(LocalDateTime.now());

        rideRepository.save(ride);

        RideResponse response=new RideResponse();
        response.setRideId(ride.getId());
        response.setDriverId(driver.getId());
        response.setDriverName(driver.getName());
        response.setStatus(ride.getStatus().name());

        return response;
    }

    @Override
    public Ride getAssignedRide(String driverEmail) {
        Driver driver=driverRepository.findByEmail(driverEmail)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        return rideRepository
                .findByDriverIdAndStatus(driver.getId(), RideStatus.ASSIGNED)
                .orElseThrow(() -> new RuntimeException("No assigned ride"));
    }

    @Override
    public void startRide(Long rideId, String driverEmail) {
        Ride ride=rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        if(!ride.getDriver().getEmail().equals(driverEmail)){
            throw new RuntimeException("Unauthorized driver");
        }
        if(ride.getStatus() != RideStatus.ASSIGNED){
            throw new RuntimeException("Ride cannot be started");
        }
        ride.setStatus(RideStatus.STARTED);
        rideRepository.save(ride);
    }

    @Override
    public void completeRide(Long rideId, String driverEmail) {

        Ride ride=rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if(!ride.getDriver().getEmail().equals(driverEmail)){
            throw new RuntimeException("Unauthorized driver");
        }

        if(ride.getStatus() != RideStatus.STARTED){
            throw new RuntimeException("Ride not started");
        }

        ride.setStatus(RideStatus.COMPLETED);

        Driver driver=ride.getDriver();
        driver.setAvailable(true);

        driverRepository.save(driver);
        rideRepository.save(ride);
    }

//    @Override
//    public RideResponse viewRide(Long rideId, String customerEmail) {
//
//        Ride ride = rideRepository.findById(rideId)
//                .orElseThrow(() -> new RuntimeException("Ride not found"));
//
//        // ⭐ Customer can only view their own rides
//        if (!ride.getCustomer().getEmail().equals(customerEmail)) {
//            throw new RuntimeException("Unauthorized access");
//        }
//
//        RideResponse response = new RideResponse();
//        response.setRideId(ride.getId());
//        response.setDriverId(ride.getDriver().getId());
//        response.setDriverName(ride.getDriver().getName());
//        response.setVehicleNumber(ride.getDriver().getVehicleNumber());
//        response.setStatus(ride.getStatus().name());
//
//        // ⭐ Get driver's live location
//        driverLocationRepository.findByDriverId(ride.getDriver().getId())
//                .ifPresent(loc -> {
//                    response.setDriverLat(loc.getLatitude());
//                    response.setDriverLng(loc.getLongitude());
//                });
//
//        return response;
//    }

    @Override
    public RideResponse viewRide(Long rideId, String customerEmail) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (!ride.getCustomer().getEmail().equals(customerEmail)) {
            throw new RuntimeException("Unauthorized access");
        }

        RideResponse response = new RideResponse();
        response.setRideId(ride.getId());
        response.setDriverId(ride.getDriver().getId());
        response.setDriverName(ride.getDriver().getName());
        response.setVehicleNumber(ride.getDriver().getVehicleNumber());
        response.setStatus(ride.getStatus().name());

        // ✅ ADD THESE (CRITICAL)
        response.setPickupLat(ride.getPickupLat());
        response.setPickupLng(ride.getPickupLng());
        response.setDropLat(ride.getDropLat());
        response.setDropLng(ride.getDropLng());

        // ✅ Driver live location
        driverLocationRepository.findByDriverId(ride.getDriver().getId())
                .ifPresent(loc -> {
                    response.setDriverLat(loc.getLatitude());
                    response.setDriverLng(loc.getLongitude());
                });

        return response;
    }


}
