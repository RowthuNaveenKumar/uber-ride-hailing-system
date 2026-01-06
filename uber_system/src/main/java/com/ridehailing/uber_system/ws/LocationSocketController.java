package com.ridehailing.uber_system.ws;

import com.ridehailing.uber_system.dto.DriverLocationMessage;
import com.ridehailing.uber_system.model.Driver;
import com.ridehailing.uber_system.model.DriverLocation;
import com.ridehailing.uber_system.repository.DriverLocationRepository;
import com.ridehailing.uber_system.repository.DriverRepository;
import com.ridehailing.uber_system.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class LocationSocketController {

    private final DriverLocationRepository repo;
    private final JwtService jwtService;
    private final DriverRepository driverRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public LocationSocketController(DriverLocationRepository repo, JwtService jwtService, DriverRepository driverRepository) {
        this.repo = repo;
        this.jwtService = jwtService;
        this.driverRepository = driverRepository;
    }

    @MessageMapping("/location/update")
    public void updateLocation(DriverLocationMessage message) {

        String token = message.getToken().substring(7);
        String email = jwtService.extractUsername(token);
        String role  = jwtService.extractRole(token);

        if (!"DRIVER".equals(role)) {
            throw new RuntimeException("Unauthorized");
        }

        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow();

        DriverLocation location = repo.findByDriverId(driver.getId())
                .orElse(new DriverLocation());

        location.setDriverId(driver.getId());
        location.setLatitude(message.getLatitude());
        location.setLongitude(message.getLongitude());
        location.setLastUpdated(LocalDateTime.now());

        repo.save(location);

        messagingTemplate.convertAndSend(
                "/topic/location/" + driver.getId(),
                message
        );
    }
}
