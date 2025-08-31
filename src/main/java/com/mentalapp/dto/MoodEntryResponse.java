package com.mentalapp.dto;

import com.mentalapp.entity.MoodEntry;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodEntryResponse {

    private Long id;
    private List<String> emotionKeys;
    private String location;
    private MoodEntry.Environment environment;
    private String description;
    private Integer energyLevel;
    private Boolean isVoiceInput;
    private LocalDateTime entryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer suggestedActivitiesCount;
}
