package com.wfit.springbootbookstores.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ComplaintSuggestion")
public class ComplaintSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", nullable = false, length = 10)
    private ProcessingStatus processingStatus;

    @Column(name = "submission_time", nullable = false)
    private LocalDateTime submissionTime;

    public enum ProcessingStatus {
        PENDING,
        PROCESSED
    }
}
