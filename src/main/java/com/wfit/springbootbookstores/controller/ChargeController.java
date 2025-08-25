package com.wfit.springbootbookstores.controller;

import com.wfit.springbootbookstores.entity.Charge;
import com.wfit.springbootbookstores.entity.User;
import com.wfit.springbootbookstores.service.ChargeService;
import com.wfit.springbootbookstores.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/charges")
public class ChargeController {

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createCharge(@RequestBody Charge charge) {
        if (charge.getOwner() == null || charge.getOwner().getId() == null) {
            return ResponseEntity.badRequest().body("Owner ID is required for charge creation.");
        }
        Optional<User> ownerOptional = userService.getUserById(charge.getOwner().getId());
        if (!ownerOptional.isPresent() || ownerOptional.get().getUserType() != User.UserType.OWNER) {
            return ResponseEntity.badRequest().body("Invalid or non-owner user specified as owner.");
        }
        charge.setOwner(ownerOptional.get());
        charge.setPaymentStatus(Charge.PaymentStatus.UNPAID); // Default to UNPAID
        charge.setPaymentTime(null); // Payment time is null until paid
        try {
            Charge savedCharge = chargeService.saveCharge(charge);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCharge);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Charge> getChargeById(@PathVariable Integer id) {
        Optional<Charge> charge = chargeService.getChargeById(id);
        return charge.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCharge(@PathVariable Integer id, @RequestBody Charge charge) {
        Optional<Charge> existingChargeOptional = chargeService.getChargeById(id);
        if (existingChargeOptional.isPresent()) {
            Charge existingCharge = existingChargeOptional.get();
            
            // 只更新非null的字段，避免覆盖现有数据
            if (charge.getChargeItem() != null) {
                existingCharge.setChargeItem(charge.getChargeItem());
            }
            if (charge.getAmount() != null) {
                existingCharge.setAmount(charge.getAmount());
            }
            if (charge.getDescription() != null) {
                existingCharge.setDescription(charge.getDescription());
            }

            // Only allow changing payment status if it's from UNPAID to PAID and handle payment logic
            if (charge.getPaymentStatus() == Charge.PaymentStatus.PAID && existingCharge.getPaymentStatus() == Charge.PaymentStatus.UNPAID) {
                // This is where actual payment logic would go, potentially involving user balance deduction.
                // For now, we'll just update status and time.
                existingCharge.setPaymentStatus(Charge.PaymentStatus.PAID);
                existingCharge.setPaymentTime(LocalDateTime.now());
            } else if (charge.getPaymentStatus() != null) {
                existingCharge.setPaymentStatus(charge.getPaymentStatus()); // Allow other status changes, e.g., if re-marking as UNPAID
            }

            // If owner is being changed, validate it
            if (charge.getOwner() != null && charge.getOwner().getId() != null) {
                Optional<User> newOwnerOptional = userService.getUserById(charge.getOwner().getId());
                if (!newOwnerOptional.isPresent() || newOwnerOptional.get().getUserType() != User.UserType.OWNER) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid or non-owner user specified as new owner."));
                }
                existingCharge.setOwner(newOwnerOptional.get());
            }

            Charge updatedCharge = chargeService.saveCharge(existingCharge);
            return ResponseEntity.ok(updatedCharge);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharge(@PathVariable Integer id) {
        if (chargeService.getChargeById(id).isPresent()) {
            chargeService.deleteCharge(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Charge>> getAllCharges() {
        List<Charge> charges = chargeService.getAllCharges();
        return ResponseEntity.ok(charges);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getChargesByOwner(@PathVariable Integer ownerId) {
        Optional<User> ownerOptional = userService.getUserById(ownerId);
        if (!ownerOptional.isPresent() || ownerOptional.get().getUserType() != User.UserType.OWNER) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or non-owner user ID."));
        }
        List<Charge> charges = chargeService.getChargesByOwner(ownerOptional.get());
        return ResponseEntity.ok(charges);
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payCharge(@PathVariable Integer id) {
        try {
            // In a real scenario, this would involve deducting from owner's balance
            // For now, we'll just update status and payment time
            Charge updatedCharge = chargeService.updatePaymentStatus(id, Charge.PaymentStatus.PAID);
            updatedCharge.setPaymentTime(LocalDateTime.now()); // Set payment time upon successful pay
            chargeService.saveCharge(updatedCharge); // Save again to persist payment time
            return ResponseEntity.ok(updatedCharge);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
