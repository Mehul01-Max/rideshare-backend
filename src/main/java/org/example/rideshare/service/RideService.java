package org.example.rideshare.service;

import org.example.rideshare.dto.CreateRideRequest;
import org.example.rideshare.exception.BadRequestException;
import org.example.rideshare.exception.NotFoundException;
import org.example.rideshare.model.Ride;
import org.example.rideshare.model.User;
import org.example.rideshare.repository.RideRepository;
import org.example.rideshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserRepository userRepository;

    public Ride createRide(CreateRideRequest request, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        Ride ride = new Ride(user.getId(), null, request.getPickupLocation(), request.getDropLocation(), "REQUESTED",
                new Date());
        return rideRepository.save(ride);
    }

    public List<Ride> getPendingRides() {
        return rideRepository.findByStatus("REQUESTED");
    }

    public Ride acceptRide(String rideId, String username) {
        User driver = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Driver not found"));
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new NotFoundException("Ride not found"));
        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not available for acceptance");
        }
        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");
        return rideRepository.save(ride);
    }

    public Ride completeRide(String rideId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new NotFoundException("Ride not found"));
        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("Ride is not accepted yet");
        }
        ride.setStatus("COMPLETED");
        return rideRepository.save(ride);
    }

    public List<Ride> getUserRides(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        return rideRepository.findByUserId(user.getId());
    }
}
