package com.wfit.springbootbookstores.service;

import com.wfit.springbootbookstores.entity.ComplaintSuggestion;
import com.wfit.springbootbookstores.entity.User;

import java.util.List;
import java.util.Optional;

public interface ComplaintSuggestionService {
    ComplaintSuggestion saveComplaintSuggestion(ComplaintSuggestion complaintSuggestion);
    Optional<ComplaintSuggestion> getComplaintSuggestionById(Integer id);
    void deleteComplaintSuggestion(Integer id);
    List<ComplaintSuggestion> getAllComplaintSuggestions();
    List<ComplaintSuggestion> getComplaintSuggestionsByOwner(User owner);
    ComplaintSuggestion updateProcessingStatus(Integer complaintId, ComplaintSuggestion.ProcessingStatus status);
}
