package com.wfit.springbootbookstores.controller;

import com.wfit.springbootbookstores.entity.Property;
import com.wfit.springbootbookstores.entity.User;
import com.wfit.springbootbookstores.service.PropertyService;
import com.wfit.springbootbookstores.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private UserService userService; // To fetch Owner by ID if needed

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createProperty(@RequestBody Property property) {
        // In a real application, you'd likely get the owner from security context or ensure owner exists
        if (property.getOwner() == null || property.getOwner().getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Owner ID is required for property creation."));
        }
        Optional<User> ownerOptional = userService.getUserById(property.getOwner().getId());
        if (!ownerOptional.isPresent() || ownerOptional.get().getUserType() != User.UserType.OWNER) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or non-owner user specified as owner."));
        }
        property.setOwner(ownerOptional.get());
        try {
            Property savedProperty = propertyService.saveProperty(property);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProperty);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Integer id) {
        Optional<Property> property = propertyService.getPropertyById(id);
        return property.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProperty(@PathVariable Integer id, @RequestBody Property property) {
        Optional<Property> existingPropertyOptional = propertyService.getPropertyById(id);
        if (existingPropertyOptional.isPresent()) {
            Property existingProperty = existingPropertyOptional.get();
            // Update fields that are allowed to be modified
            existingProperty.setHouseNumber(property.getHouseNumber());
            existingProperty.setBuilding(property.getBuilding());
            existingProperty.setUnit(property.getUnit());
            existingProperty.setArea(property.getArea());
            existingProperty.setHouseType(property.getHouseType());
            existingProperty.setAdditionalDescription(property.getAdditionalDescription());
            existingProperty.setOccupancyInfo(property.getOccupancyInfo());

            // If owner is being changed, validate it
            if (property.getOwner() != null && property.getOwner().getId() != null) {
                Optional<User> newOwnerOptional = userService.getUserById(property.getOwner().getId());
                if (!newOwnerOptional.isPresent() || newOwnerOptional.get().getUserType() != User.UserType.OWNER) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid or non-owner user specified as new owner."));
                }
                existingProperty.setOwner(newOwnerOptional.get());
            }

            Property updatedProperty = propertyService.saveProperty(existingProperty);
            return ResponseEntity.ok(updatedProperty);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Integer id) {
        if (propertyService.getPropertyById(id).isPresent()) {
            propertyService.deleteProperty(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Property>> getAllProperties() {
        List<Property> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getPropertiesByOwner(@PathVariable Integer ownerId) {
        Optional<User> ownerOptional = userService.getUserById(ownerId);
        if (!ownerOptional.isPresent() || ownerOptional.get().getUserType() != User.UserType.OWNER) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or non-owner user ID."));
        }
        List<Property> properties = propertyService.getPropertiesByOwner(ownerOptional.get());
        return ResponseEntity.ok(properties);
    }
}
