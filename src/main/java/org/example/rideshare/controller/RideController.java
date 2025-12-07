package org.example.rideshare.controller;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.model.Ride;
import org.example.rideshare.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RideController {

    @Autowired
    private RideService rideService;

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
}
