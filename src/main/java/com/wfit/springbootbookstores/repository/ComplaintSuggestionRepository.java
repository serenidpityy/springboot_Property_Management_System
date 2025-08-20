package com.wfit.springbootbookstores.repository;

import com.wfit.springbootbookstores.entity.ComplaintSuggestion;
import com.wfit.springbootbookstores.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintSuggestionRepository extends JpaRepository<ComplaintSuggestion, Integer> {
    List<ComplaintSuggestion> findByOwner(User owner);
}
