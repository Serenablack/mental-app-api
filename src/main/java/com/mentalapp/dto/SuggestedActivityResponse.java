package com.mentalapp.dto;

import com.mentalapp.entity.SuggestedActivity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestedActivityResponse {

    private Long id;
    private String title;
    private String description;
    private SuggestedActivity.ActivityCategory category;
    private Integer estimatedDurationMinutes;
    private SuggestedActivity.DifficultyLevel difficultyLevel;
    private Boolean isCompleted;
    private LocalDateTime completedAt;
    private LocalDate suggestedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long moodEntryId;
}
