package com.ridehailing.uber_system.service.impl;

import com.ridehailing.uber_system.dto.DriverLocationUpdateRequest;
import com.ridehailing.uber_system.dto.NearbyDriverResponse;
import com.ridehailing.uber_system.model.Driver;
import com.ridehailing.uber_system.model.DriverLocation;
import com.ridehailing.uber_system.model.Ride;
import com.ridehailing.uber_system.model.RideStatus;
import com.ridehailing.uber_system.repository.DriverLocationRepository;
import com.ridehailing.uber_system.repository.DriverRepository;
import com.ridehailing.uber_system.repository.RideRepository;
import com.ridehailing.uber_system.service.DriverLocationService;
import com.ridehailing.uber_system.util.DistanceUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DriverLocationServiceImpl implements DriverLocationService {

    private final DriverRepository driverRepository;
    private final DriverLocationRepository locationRepository;
    private final RideRepository rideRepository;

    public DriverLocationServiceImpl(DriverRepository driverRepository, DriverLocationRepository locationRepository, RideRepository rideRepository) {
        this.driverRepository = driverRepository;
        this.locationRepository = locationRepository;
        this.rideRepository = rideRepository;
    }

    @Override
    public String updateLocation(String email, DriverLocationUpdateRequest request) {

        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        Ride activeRide = rideRepository
                .findTopByDriverIdAndStatusIn(
                        driver.getId(),
                        List.of(RideStatus.ASSIGNED, RideStatus.STARTED)
                )
                .orElse(null);

        // ✅ Allow update if:
        // - driver is online
        // - OR driver has active ride
        if (!driver.isAvailable() && activeRide == null) {
            throw new RuntimeException("Driver cannot update location");
        }

        DriverLocation location = locationRepository
                .findByDriverId(driver.getId())
                .orElse(new DriverLocation());

        location.setDriverId(driver.getId());
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        location.setLastUpdated(LocalDateTime.now());

        locationRepository.save(location);

        return "Location updated successfully";
    }


    @Override
    public List<NearbyDriverResponse> findNearbyDrivers(double lat, double lng) {
        List<DriverLocation> locations = locationRepository.findAll();

        return locations.stream().map(loc -> {

            Driver driver = driverRepository.findById(loc.getDriverId()).orElse(null);

            if (driver == null || !driver.isAvailable()) {
                return null;
            }

            double distance = DistanceUtil.calculate(lat, lng, loc.getLatitude(), loc.getLongitude());

            NearbyDriverResponse dto = new NearbyDriverResponse();
            dto.setDriverId(driver.getId());
            dto.setName(driver.getName());
            dto.setLatitude(loc.getLatitude());
            dto.setLongitude(loc.getLongitude());
            dto.setDistanceKm(distance);

            return dto;
        }).filter(dto -> dto != null).sorted((a, b) -> Double.compare(a.getDistanceKm(), b.getDistanceKm())).limit(5).toList();
    }

    @Override
    public DriverLocation getDriverLocationForRide(Long rideId, String customerEmail) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));

        // 🔒 Security: customer can see ONLY their ride
        if (!ride.getCustomer().getEmail().equals(customerEmail)) {
            throw new RuntimeException("Unauthorized access");
        }
        if (ride.getDriver() == null) {
            throw new RuntimeException("Driver not assigned yet");
        }
        return locationRepository.findByDriverId(ride.getDriver().getId()).orElseThrow(() -> new RuntimeException("Driver location not available"));
    }
}
