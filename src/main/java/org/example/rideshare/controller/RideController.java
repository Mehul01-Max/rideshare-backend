package org.example.rideshare.controller;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.model.Ride;
import org.example.rideshare.service.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    @Autowired
    private RideService rideService;

    private static final Logger logger = LoggerFactory.getLogger(RideController.class);

    @PostMapping("/rides")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Ride> createRide(@Valid @RequestBody CreateRideRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        Ride ride = rideService.createRide(request, username);
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/driver/rides/requests")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<Ride>> getPendingRides() {
        List<Ride> rides = rideService.getPendingRides();
        return ResponseEntity.ok(rides);
    }

    @PostMapping("/driver/rides/{rideId}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<Ride> acceptRide(@PathVariable String rideId, Authentication authentication) {
        String username = authentication.getName();
        Ride ride = rideService.acceptRide(rideId, username);
        return ResponseEntity.ok(ride);
    }

    @PostMapping("/rides/{rideId}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable String rideId) {
        Ride ride = rideService.completeRide(rideId);
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/user/rides")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Ride>> getUserRides(Authentication authentication) {
        String username = authentication.getName();
        List<Ride> rides = rideService.getUserRides(username);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> welcome(HttpServletRequest request) {
        logger.info("Request received: {} {}", request.getMethod(), request.getRequestURI());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to the Rideshare API!");
        return ResponseEntity.ok(response);
    }
}
