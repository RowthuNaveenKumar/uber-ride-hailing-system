package com.ridehailing.uber_system.controller;

import com.ridehailing.uber_system.dto.RideRequest;
import com.ridehailing.uber_system.dto.RideResponse;
import com.ridehailing.uber_system.model.Ride;
import com.ridehailing.uber_system.service.RideService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ride")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @GetMapping("/test")
    public String testRide() {
        return "Ride service accessed by CUSTOMER!";
    }

    @PostMapping("/request")
    public RideResponse requestRide(@RequestBody RideRequest request, Authentication authentication){
        return rideService.requestRide(request, authentication.getName());
    }

    @GetMapping("/driver/assigned")
    public Ride getAssignedRide(Authentication auth){
        return rideService.getAssignedRide(auth.getName());
    }

    @PostMapping("/driver/start/{rideId}")
    public String startRide(@PathVariable Long rideId,Authentication auth){
        rideService.startRide(rideId, auth.getName());
        return "Ride started";
    }

    @PostMapping("/driver/complete/{rideId}")
    public String completeRide(@PathVariable Long rideId,Authentication auth){
        rideService.completeRide(rideId, auth.getName());
        return "Ride completed";
    }

    @GetMapping("/view/{rideId}")
    public RideResponse viewRide(@PathVariable Long rideId, Authentication auth) {
        return rideService.viewRide(rideId, auth.getName());
    }

}
