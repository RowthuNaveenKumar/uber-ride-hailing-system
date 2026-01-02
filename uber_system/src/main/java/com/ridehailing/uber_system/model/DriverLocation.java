package com.ridehailing.uber_system.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "driver_location")
public class DriverLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne
//    @JoinColumn(name = "driver_id", nullable = false, unique = true)
//    private Driver driver;

    @Column(nullable = false,unique = true)
    private Long driverId;  // 👈 IMPORTANT

    private double latitude;
    private double longitude;

    private LocalDateTime lastUpdated;

    //getters & setters
}
