package com.wfit.springbootbookstores.service;

import com.wfit.springbootbookstores.entity.CommunityAnnouncement;

import java.util.List;
import java.util.Optional;

public interface CommunityAnnouncementService {
    CommunityAnnouncement saveAnnouncement(CommunityAnnouncement announcement);
    Optional<CommunityAnnouncement> getAnnouncementById(Integer id);
    void deleteAnnouncement(Integer id);
    List<CommunityAnnouncement> getAllAnnouncements();
}
