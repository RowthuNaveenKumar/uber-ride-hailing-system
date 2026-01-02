package com.ridehailing.uber_system.service.impl;

import com.ridehailing.uber_system.dto.DriverLocationUpdateRequest;
import com.ridehailing.uber_system.dto.NearbyDriverResponse;
import com.ridehailing.uber_system.model.Driver;
import com.ridehailing.uber_system.model.DriverLocation;
import com.ridehailing.uber_system.repository.DriverLocationRepository;
import com.ridehailing.uber_system.repository.DriverRepository;
import com.ridehailing.uber_system.service.DriverLocationService;
import com.ridehailing.uber_system.util.DistanceUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DriverLocationServiceImpl implements DriverLocationService {

    private final DriverRepository driverRepository;
    private final DriverLocationRepository locationRepository;

    public DriverLocationServiceImpl(DriverRepository driverRepository, DriverLocationRepository locationRepository) {
        this.driverRepository = driverRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public String updateLocation(String email, DriverLocationUpdateRequest request) {
        Driver driver=driverRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("Driver not found"));
        if(!driver.isAvailable()){
            throw new RuntimeException("Driver must be ONLINE to update location");
        }
        DriverLocation location=locationRepository
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
        List<DriverLocation> locations=locationRepository.findAll();

        return locations.stream()
                .map(loc -> {

                    Driver driver = driverRepository.findById(loc.getDriverId())
                            .orElse(null);

                    if (driver == null || !driver.isAvailable()) {
                        return null;
                    }

                    double distance = DistanceUtil.calculate(
                            lat, lng,
                            loc.getLatitude(),
                            loc.getLongitude()
                    );

                    NearbyDriverResponse dto = new NearbyDriverResponse();
                    dto.setDriverId(driver.getId());
                    dto.setName(driver.getName());
                    dto.setLatitude(loc.getLatitude());
                    dto.setLongitude(loc.getLongitude());
                    dto.setDistanceKm(distance);

                    return dto;
                })
                .filter(dto -> dto != null)
                .sorted((a, b) ->
                        Double.compare(a.getDistanceKm(), b.getDistanceKm()))
                .limit(5)
                .toList();
    }
}
