package com.mentalapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoodEntryResponse {

    private Long id;
    private Long userId;
    private String username;
    private LocalDateTime entryDate;
    private String location;
    private String comfortEnvironment;
    private String description;
    private Integer energyLevel;
    private String passion;
    private Set<EmotionResponse> emotions;
    private Set<SuggestedActivityResponse> suggestedActivities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isFromToday;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmotionResponse {
        private Long id;
        private String key;
        private String label;
        private String parentKey;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuggestedActivityResponse {
        private Long id;
        private String activityDescription;
        private Boolean isCompleted;
        private LocalDateTime completedAt;
        private String activityType;
        private Integer estimatedDurationMinutes;
        private Integer difficultyLevel;
        private Integer priorityLevel;
        private String status;
        private LocalDateTime createdAt;
    }
}
