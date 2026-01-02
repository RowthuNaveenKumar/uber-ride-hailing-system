package com.ridehailing.uber_system.controller;

import com.ridehailing.uber_system.dto.DriverLocationUpdateRequest;
import com.ridehailing.uber_system.dto.NearbyDriverResponse;
import com.ridehailing.uber_system.service.DriverLocationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/driver/location")
public class DriverLocationController {

    private final DriverLocationService locationService;

    public DriverLocationController(DriverLocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/update")
    public String updateLocation(Authentication authentication,
                                 @RequestBody DriverLocationUpdateRequest request){
        String email=authentication.getName();
        return locationService.updateLocation(email,request);
    }

    @GetMapping("/nearby")
    public List<NearbyDriverResponse> nearbyDrivers(@RequestParam double lat,@RequestParam double lng){
        return locationService.findNearbyDrivers(lat,lng);
    }

}
