package com.wfit.springbootbookstores.controller;

import com.wfit.springbootbookstores.entity.ComplaintSuggestion;
import com.wfit.springbootbookstores.entity.User;
import com.wfit.springbootbookstores.service.ComplaintSuggestionService;
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
@RequestMapping("/api/complaints")
public class ComplaintSuggestionController {

    @Autowired
    private ComplaintSuggestionService complaintSuggestionService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createComplaintSuggestion(@RequestBody ComplaintSuggestion complaintSuggestion) {
        if (complaintSuggestion.getOwner() == null || complaintSuggestion.getOwner().getId() == null) {
            return ResponseEntity.badRequest().body("Owner ID is required for complaint/suggestion creation.");
        }
        Optional<User> ownerOptional = userService.getUserById(complaintSuggestion.getOwner().getId());
        if (!ownerOptional.isPresent() || ownerOptional.get().getUserType() != User.UserType.OWNER) {
            return ResponseEntity.badRequest().body("Invalid or non-owner user specified as owner.");
        }
        complaintSuggestion.setOwner(ownerOptional.get());
        complaintSuggestion.setSubmissionTime(LocalDateTime.now()); // Set submission time on creation
        complaintSuggestion.setProcessingStatus(ComplaintSuggestion.ProcessingStatus.PENDING); // Default to PENDING
        try {
            ComplaintSuggestion savedComplaint = complaintSuggestionService.saveComplaintSuggestion(complaintSuggestion);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedComplaint);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintSuggestion> getComplaintSuggestionById(@PathVariable Integer id) {
        Optional<ComplaintSuggestion> complaintSuggestion = complaintSuggestionService.getComplaintSuggestionById(id);
        return complaintSuggestion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComplaintSuggestion(@PathVariable Integer id, @RequestBody ComplaintSuggestion complaintSuggestion) {
        Optional<ComplaintSuggestion> existingComplaintOptional = complaintSuggestionService.getComplaintSuggestionById(id);
        if (existingComplaintOptional.isPresent()) {
            ComplaintSuggestion existingComplaint = existingComplaintOptional.get();
            existingComplaint.setContent(complaintSuggestion.getContent());

            // Allow updating status, for example, by staff/admin
            if (complaintSuggestion.getProcessingStatus() != null) {
                existingComplaint.setProcessingStatus(complaintSuggestion.getProcessingStatus());
            }

            // If owner is being changed, validate it (unlikely for complaints, but for completeness)
            if (complaintSuggestion.getOwner() != null && complaintSuggestion.getOwner().getId() != null) {
                Optional<User> newOwnerOptional = userService.getUserById(complaintSuggestion.getOwner().getId());
                if (!newOwnerOptional.isPresent() || newOwnerOptional.get().getUserType() != User.UserType.OWNER) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid or non-owner user specified as new owner."));
                }
                existingComplaint.setOwner(newOwnerOptional.get());
            }

            ComplaintSuggestion updatedComplaint = complaintSuggestionService.saveComplaintSuggestion(existingComplaint);
            return ResponseEntity.ok(updatedComplaint);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaintSuggestion(@PathVariable Integer id) {
        if (complaintSuggestionService.getComplaintSuggestionById(id).isPresent()) {
            complaintSuggestionService.deleteComplaintSuggestion(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ComplaintSuggestion>> getAllComplaintSuggestions() {
        List<ComplaintSuggestion> complaints = complaintSuggestionService.getAllComplaintSuggestions();
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getComplaintSuggestionsByOwner(@PathVariable Integer ownerId) {
        Optional<User> ownerOptional = userService.getUserById(ownerId);
        if (!ownerOptional.isPresent() || ownerOptional.get().getUserType() != User.UserType.OWNER) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid or non-owner user ID."));
        }
        List<ComplaintSuggestion> complaints = complaintSuggestionService.getComplaintSuggestionsByOwner(ownerOptional.get());
        return ResponseEntity.ok(complaints);
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<?> processComplaint(@PathVariable Integer id) {
        try {
            ComplaintSuggestion updatedComplaint = complaintSuggestionService.updateProcessingStatus(id, ComplaintSuggestion.ProcessingStatus.PROCESSED);
            return ResponseEntity.ok(updatedComplaint);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
