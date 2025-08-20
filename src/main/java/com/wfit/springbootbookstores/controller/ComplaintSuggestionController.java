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
        try {
            // 验证owner信息
            if (complaintSuggestion.getOwner() == null || complaintSuggestion.getOwner().getId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Owner ID is required for complaint/suggestion creation."));
            }

            Integer ownerId = complaintSuggestion.getOwner().getId();
            Optional<User> ownerOptional = userService.getUserById(ownerId);
            
            if (!ownerOptional.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User with ID " + ownerId + " not found."));
            }

            User owner = ownerOptional.get();
            
            // 放宽验证：允许OWNER和STAFF类型的用户提交投诉建议
            if (owner.getUserType() != User.UserType.OWNER && owner.getUserType() != User.UserType.STAFF) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only OWNER or STAFF users can submit complaints. User type: " + owner.getUserType()));
            }

            // 设置完整的owner对象
            complaintSuggestion.setOwner(owner);
            complaintSuggestion.setSubmissionTime(LocalDateTime.now());
            complaintSuggestion.setProcessingStatus(ComplaintSuggestion.ProcessingStatus.PENDING);

            ComplaintSuggestion savedComplaint = complaintSuggestionService.saveComplaintSuggestion(complaintSuggestion);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedComplaint);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Failed to create complaint: " + e.getMessage()));
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
            
            // 更新内容（虽然前端可能设为只读，但保留更新能力）
            if (complaintSuggestion.getContent() != null) {
                existingComplaint.setContent(complaintSuggestion.getContent());
            }

            // 更新提交人电话
            if (complaintSuggestion.getSubmitterPhone() != null) {
                existingComplaint.setSubmitterPhone(complaintSuggestion.getSubmitterPhone());
            }

            // 更新紧急程度
            if (complaintSuggestion.getUrgencyLevel() != null) {
                existingComplaint.setUrgencyLevel(complaintSuggestion.getUrgencyLevel());
            }

            // 更新处理状态
            if (complaintSuggestion.getProcessingStatus() != null) {
                existingComplaint.setProcessingStatus(complaintSuggestion.getProcessingStatus());
            }

            // 更新处理人信息
            if (complaintSuggestion.getProcessor() != null && complaintSuggestion.getProcessor().getId() != null) {
                Optional<User> processorOptional = userService.getUserById(complaintSuggestion.getProcessor().getId());
                if (processorOptional.isPresent()) {
                    existingComplaint.setProcessor(processorOptional.get());
                }
            }

            // 更新处理时间
            if (complaintSuggestion.getProcessingTime() != null) {
                existingComplaint.setProcessingTime(complaintSuggestion.getProcessingTime());
            }

            // 更新处理人回应
            if (complaintSuggestion.getProcessorResponse() != null) {
                existingComplaint.setProcessorResponse(complaintSuggestion.getProcessorResponse());
            }

            // 如果owner被更改，验证新owner
            if (complaintSuggestion.getOwner() != null && complaintSuggestion.getOwner().getId() != null) {
                Optional<User> newOwnerOptional = userService.getUserById(complaintSuggestion.getOwner().getId());
                if (!newOwnerOptional.isPresent()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Invalid user specified as new owner."));
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
