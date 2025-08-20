package com.wfit.springbootbookstores.repository;

import com.wfit.springbootbookstores.entity.CommunityAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityAnnouncementRepository extends JpaRepository<CommunityAnnouncement, Integer> {
}
