package com.wfit.springbootbookstores.service.impl;

import com.wfit.springbootbookstores.entity.ComplaintSuggestion;
import com.wfit.springbootbookstores.entity.User;
import com.wfit.springbootbookstores.repository.ComplaintSuggestionRepository;
import com.wfit.springbootbookstores.service.ComplaintSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintSuggestionServiceImpl implements ComplaintSuggestionService {

    @Autowired
    private ComplaintSuggestionRepository complaintSuggestionRepository;

    @Override
    public ComplaintSuggestion saveComplaintSuggestion(ComplaintSuggestion complaintSuggestion) {
        return complaintSuggestionRepository.save(complaintSuggestion);
    }

    @Override
    public Optional<ComplaintSuggestion> getComplaintSuggestionById(Integer id) {
        return complaintSuggestionRepository.findById(id);
    }

    @Override
    public void deleteComplaintSuggestion(Integer id) {
        complaintSuggestionRepository.deleteById(id);
    }

    @Override
    public List<ComplaintSuggestion> getAllComplaintSuggestions() {
        return complaintSuggestionRepository.findAll();
    }

    @Override
    public List<ComplaintSuggestion> getComplaintSuggestionsByOwner(User owner) {
        return complaintSuggestionRepository.findByOwner(owner);
    }

    @Override
    public ComplaintSuggestion updateProcessingStatus(Integer complaintId, ComplaintSuggestion.ProcessingStatus status) {
        ComplaintSuggestion complaintSuggestion = complaintSuggestionRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint/Suggestion not found"));
        complaintSuggestion.setProcessingStatus(status);
        return complaintSuggestionRepository.save(complaintSuggestion);
    }
}
