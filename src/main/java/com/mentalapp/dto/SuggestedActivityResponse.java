package com.mentalapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestedActivityResponse {

    private Long id;
    private Long moodEntryId;
    private String activityDescription;
    private Boolean isCompleted;
    private LocalDateTime completedAt;
    private String activityType;
    private Integer estimatedDurationMinutes;
    private Integer difficultyLevel;
    private Integer priorityLevel;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


