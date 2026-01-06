package com.ridehailing.uber_system.repository;

import com.ridehailing.uber_system.model.Ride;
import com.ridehailing.uber_system.model.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Long> {
//    Optional<Ride> findByDriverIdAndStatus(Long driverID, RideStatus status);

    Optional<Ride> findTopByDriverIdAndStatusIn(Long driverId, List<RideStatus> statuses);
}
