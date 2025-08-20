package com.wfit.springbootbookstores.controller;

import com.wfit.springbootbookstores.entity.CommunityAnnouncement;
import com.wfit.springbootbookstores.service.CommunityAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/announcements")
public class CommunityAnnouncementController {

    @Autowired
    private CommunityAnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<CommunityAnnouncement> createAnnouncement(@RequestBody CommunityAnnouncement announcement) {
        announcement.setPublishTime(LocalDateTime.now()); // Set publish time on creation
        CommunityAnnouncement savedAnnouncement = announcementService.saveAnnouncement(announcement);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnnouncement);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommunityAnnouncement> getAnnouncementById(@PathVariable Integer id) {
        Optional<CommunityAnnouncement> announcement = announcementService.getAnnouncementById(id);
        return announcement.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommunityAnnouncement> updateAnnouncement(@PathVariable Integer id, @RequestBody CommunityAnnouncement announcement) {
        Optional<CommunityAnnouncement> existingAnnouncementOptional = announcementService.getAnnouncementById(id);
        if (existingAnnouncementOptional.isPresent()) {
            CommunityAnnouncement existingAnnouncement = existingAnnouncementOptional.get();
            existingAnnouncement.setTitle(announcement.getTitle());
            existingAnnouncement.setContent(announcement.getContent());
            // publishTime should probably not be changed via update, or handle it carefully

            CommunityAnnouncement updatedAnnouncement = announcementService.saveAnnouncement(existingAnnouncement);
            return ResponseEntity.ok(updatedAnnouncement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable Integer id) {
        if (announcementService.getAnnouncementById(id).isPresent()) {
            announcementService.deleteAnnouncement(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CommunityAnnouncement>> getAllAnnouncements() {
        List<CommunityAnnouncement> announcements = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(announcements);
    }
}
