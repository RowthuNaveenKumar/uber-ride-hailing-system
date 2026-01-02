package com.ridehailing.uber_system.repository;

import com.ridehailing.uber_system.model.Driver;
import com.ridehailing.uber_system.model.DriverLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverLocationRepository extends JpaRepository<DriverLocation,Long> {
    Optional<DriverLocation> findByDriverId(Long driverId);
}
