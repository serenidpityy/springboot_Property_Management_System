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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "submitter_phone", length = 20)
    private String submitterPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level", nullable = false, length = 10)
    private UrgencyLevel urgencyLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", nullable = false, length = 10)
    private ProcessingStatus processingStatus;

    @Column(name = "submission_time", nullable = false)
    private LocalDateTime submissionTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "processor_id")
    private User processor;

    @Column(name = "processing_time")
    private LocalDateTime processingTime;

    @Column(name = "processor_response", columnDefinition = "TEXT")
    private String processorResponse;

    public enum ProcessingStatus {
        PENDING,
        PROCESSED
    }

    public enum UrgencyLevel {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    @PrePersist
    public void prePersist() {
        if (submissionTime == null) {
            submissionTime = LocalDateTime.now();
        }
        if (processingStatus == null) {
            processingStatus = ProcessingStatus.PENDING;
        }
        if (urgencyLevel == null) {
            urgencyLevel = UrgencyLevel.LOW;
        }
    }

    @PreUpdate
    public void preUpdate() {
        // 如果状态改为已处理且处理时间为空，设置处理时间
        if (processingStatus == ProcessingStatus.PROCESSED && processingTime == null) {
            processingTime = LocalDateTime.now();
        }
    }
}
