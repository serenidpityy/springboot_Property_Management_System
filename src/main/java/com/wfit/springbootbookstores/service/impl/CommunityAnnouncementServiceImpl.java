package com.wfit.springbootbookstores.service.impl;

import com.wfit.springbootbookstores.entity.CommunityAnnouncement;
import com.wfit.springbootbookstores.repository.CommunityAnnouncementRepository;
import com.wfit.springbootbookstores.service.CommunityAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityAnnouncementServiceImpl implements CommunityAnnouncementService {

    @Autowired
    private CommunityAnnouncementRepository announcementRepository;

    @Override
    public CommunityAnnouncement saveAnnouncement(CommunityAnnouncement announcement) {
        return announcementRepository.save(announcement);
    }

    @Override
    public Optional<CommunityAnnouncement> getAnnouncementById(Integer id) {
        return announcementRepository.findById(id);
    }

    @Override
    public void deleteAnnouncement(Integer id) {
        announcementRepository.deleteById(id);
    }

    @Override
    public List<CommunityAnnouncement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }
}
