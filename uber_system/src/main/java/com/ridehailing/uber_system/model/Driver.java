package com.ridehailing.uber_system.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "drivers")
@Data
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String vehicleNumber;

    private boolean available;

    @Enumerated(EnumType.STRING)
    private Role role;


    // getters & setters
}
