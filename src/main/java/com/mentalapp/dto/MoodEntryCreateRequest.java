package com.mentalapp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoodEntryCreateRequest {

    @NotNull(message = "Entry date is required")
    private LocalDateTime entryDate;

    @Size(max = 255, message = "Location must be less than 255 characters")
    private String location;

    @Size(max = 50, message = "Comfort environment must be less than 50 characters")
    private String comfortEnvironment;

    @Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description;

    @Min(value = 1, message = "Energy level must be between 1 and 5")
    @Max(value = 5, message = "Energy level must be between 1 and 5")
    private Integer energyLevel;

    @Size(max = 100, message = "Passion must be less than 100 characters")
    private String passion;

    @NotEmpty(message = "At least one emotion must be selected")
    @Size(min = 2, message = "At least two emotions must be selected")
    private Set<Long> emotionIds = new HashSet<>();
}