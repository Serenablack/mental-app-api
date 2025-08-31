package com.mentalapp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoodEntryUpdateRequest {

    @Size(max = 255, message = "Location cannot exceed 255 characters")
    private String location;

    @Pattern(regexp = "^(alone|in_group)$", message = "Comfort environment must be 'alone' or 'in_group'")
    private String comfortEnvironment;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    @Min(value = 1, message = "Energy level must be between 1 and 5")
    @Max(value = 5, message = "Energy level must be between 1 and 5")
    private Integer energyLevel;

    @Size(max = 100, message = "Passion cannot exceed 100 characters")
    private String passion;

    @Size(min = 2, message = "At least two emotions must be selected")
    private Set<Long> emotionIds; // IDs of selected emotions

    // Optional fields for future enhancement
    private Boolean isVoiceInput = false;
    private String voiceInputUrl;
}
