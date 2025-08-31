package com.mentalapp.dto;

import com.mentalapp.entity.MoodEntry;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodEntryCreateRequest {

    @NotEmpty(message = "At least one emotion must be selected")
    @Size(min = 1, max = 10, message = "You can select between 1 and 10 emotions")
    private List<String> emotionKeys;

    @Size(max = 500, message = "Location must be less than 500 characters")
    private String location;

    @NotNull(message = "Environment selection is required")
    private MoodEntry.Environment environment;

    @Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description;

    @NotNull(message = "Energy level is required")
    @Min(value = 1, message = "Energy level must be between 1 and 5")
    @Max(value = 5, message = "Energy level must be between 1 and 5")
    private Integer energyLevel;

    private Boolean isVoiceInput = false;
}
