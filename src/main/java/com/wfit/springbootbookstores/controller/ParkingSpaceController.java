package com.wfit.springbootbookstores.controller;

import com.wfit.springbootbookstores.entity.ParkingSpace;
import com.wfit.springbootbookstores.entity.User;
import com.wfit.springbootbookstores.service.ParkingSpaceService;
import com.wfit.springbootbookstores.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/parking")
public class ParkingSpaceController {

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createParkingSpace(@RequestBody ParkingSpace parkingSpace) {
        if (parkingSpace.getOwner() == null || parkingSpace.getOwner().getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Owner ID is required for parking space creation."));
        }
        Optional<User> ownerOptional = userService.getUserById(parkingSpace.getOwner().getId());
        if (!ownerOptional.isPresent() || ownerOptional.get().getUserType() != User.UserType.OWNER) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or non-owner user specified as owner."));
        }
        parkingSpace.setOwner(ownerOptional.get());
        try {
            ParkingSpace savedParkingSpace = parkingSpaceService.saveParkingSpace(parkingSpace);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedParkingSpace);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpace> getParkingSpaceById(@PathVariable Integer id) {
        Optional<ParkingSpace> parkingSpace = parkingSpaceService.getParkingSpaceById(id);
        return parkingSpace.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateParkingSpace(@PathVariable Integer id, @RequestBody ParkingSpace parkingSpace) {
        Optional<ParkingSpace> existingParkingSpaceOptional = parkingSpaceService.getParkingSpaceById(id);
        if (existingParkingSpaceOptional.isPresent()) {
            ParkingSpace existingParkingSpace = existingParkingSpaceOptional.get();
            existingParkingSpace.setParkingId(parkingSpace.getParkingId());

            if (parkingSpace.getOwner() != null && parkingSpace.getOwner().getId() != null) {
                Optional<User> newOwnerOptional = userService.getUserById(parkingSpace.getOwner().getId());
                if (!newOwnerOptional.isPresent() || newOwnerOptional.get().getUserType() != User.UserType.OWNER) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid or non-owner user specified as new owner."));
                }
                existingParkingSpace.setOwner(newOwnerOptional.get());
            }

            ParkingSpace updatedParkingSpace = parkingSpaceService.saveParkingSpace(existingParkingSpace);
            return ResponseEntity.ok(updatedParkingSpace);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpace(@PathVariable Integer id) {
        if (parkingSpaceService.getParkingSpaceById(id).isPresent()) {
            parkingSpaceService.deleteParkingSpace(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpace>> getAllParkingSpaces() {
        List<ParkingSpace> parkingSpaces = parkingSpaceService.getAllParkingSpaces();
        return ResponseEntity.ok(parkingSpaces);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getParkingSpacesByOwner(@PathVariable Integer ownerId) {
        Optional<User> ownerOptional = userService.getUserById(ownerId);
        if (!ownerOptional.isPresent() || ownerOptional.get().getUserType() != User.UserType.OWNER) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or non-owner user ID."));
        }
        List<ParkingSpace> parkingSpaces = parkingSpaceService.getParkingSpacesByOwner(ownerOptional.get());
        return ResponseEntity.ok(parkingSpaces);
    }
}
