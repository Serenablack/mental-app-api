package com.mentalapp.controller;

import com.mentalapp.entity.MoodEntry;
import com.mentalapp.entity.User;
import com.mentalapp.service.MoodEntryService;
import com.mentalapp.dto.MoodEntryCreateRequest;
import com.mentalapp.dto.MoodEntryUpdateRequest;
import com.mentalapp.dto.MoodEntryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mood-entries")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class MoodEntryController {

    private final MoodEntryService moodEntryService;

    @PostMapping
    public ResponseEntity<MoodEntryResponse> createMoodEntry(
            @Valid @RequestBody MoodEntryCreateRequest request,
            @RequestAttribute("user") User user) {

        log.info("Creating mood entry for user: {}", user.getId());

        MoodEntry moodEntry = convertToEntity(request);
        MoodEntry savedEntry = moodEntryService.createMoodEntry(moodEntry, user);

        return ResponseEntity.ok(convertToResponse(savedEntry));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoodEntryResponse> updateMoodEntry(
            @PathVariable Long id,
            @Valid @RequestBody MoodEntryUpdateRequest request,
            @RequestAttribute("user") User user) {

        log.info("Updating mood entry: {} for user: {}", id, user.getId());

        MoodEntry updatedEntry = convertToEntity(request);
        Optional<MoodEntry> result = moodEntryService.updateMoodEntry(id, updatedEntry, user);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToResponse(result.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoodEntry(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {

        log.info("Deleting mood entry: {} for user: {}", id, user.getId());

        boolean deleted = moodEntryService.deleteMoodEntry(id, user);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MoodEntryResponse>> getAllMoodEntries(
            @RequestAttribute("user") User user) {

        List<MoodEntry> entries = moodEntryService.getUserMoodEntries(user);
        List<MoodEntryResponse> responses = entries.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/today")
    public ResponseEntity<List<MoodEntryResponse>> getTodayMoodEntries(
            @RequestAttribute("user") User user) {

        List<MoodEntry> entries = moodEntryService.getTodayMoodEntries(user);
        List<MoodEntryResponse> responses = entries.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/editable")
    public ResponseEntity<List<MoodEntryResponse>> getTodayEditableMoodEntries(
            @RequestAttribute("user") User user) {

        List<MoodEntry> entries = moodEntryService.getTodayEditableMoodEntries(user);
        List<MoodEntryResponse> responses = entries.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<MoodEntryResponse>> getMoodEntriesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestAttribute("user") User user) {

        List<MoodEntry> entries = moodEntryService.getMoodEntriesByDate(user, date);
        List<MoodEntryResponse> responses = entries.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/range")
    public ResponseEntity<List<MoodEntryResponse>> getMoodEntriesInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestAttribute("user") User user) {

        List<MoodEntry> entries = moodEntryService.getMoodEntriesInRange(user, startDate, endDate);
        List<MoodEntryResponse> responses = entries.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoodEntryResponse> getMoodEntryById(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {

        Optional<MoodEntry> entry = moodEntryService.getMoodEntryById(id, user);

        if (entry.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToResponse(entry.get()));
    }

    @GetMapping("/statistics")
    public ResponseEntity<MoodEntryService.MoodStatistics> getMoodStatistics(
            @RequestAttribute("user") User user) {

        MoodEntryService.MoodStatistics statistics = moodEntryService.getUserMoodStatistics(user);
        return ResponseEntity.ok(statistics);
    }

    private MoodEntry convertToEntity(MoodEntryCreateRequest request) {
        return MoodEntry.builder()
                .emotionKeys(request.getEmotionKeys())
                .location(request.getLocation())
                .environment(request.getEnvironment())
                .description(request.getDescription())
                .energyLevel(request.getEnergyLevel())
                .isVoiceInput(request.getIsVoiceInput())
                .build();
    }

    private MoodEntry convertToEntity(MoodEntryUpdateRequest request) {
        return MoodEntry.builder()
                .emotionKeys(request.getEmotionKeys())
                .location(request.getLocation())
                .environment(request.getEnvironment())
                .description(request.getDescription())
                .energyLevel(request.getEnergyLevel())
                .isVoiceInput(request.getIsVoiceInput())
                .build();
    }

    private MoodEntryResponse convertToResponse(MoodEntry moodEntry) {
        return MoodEntryResponse.builder()
                .id(moodEntry.getId())
                .emotionKeys(moodEntry.getEmotionKeys())
                .location(moodEntry.getLocation())
                .environment(moodEntry.getEnvironment())
                .description(moodEntry.getDescription())
                .energyLevel(moodEntry.getEnergyLevel())
                .isVoiceInput(moodEntry.getIsVoiceInput())
                .entryDate(moodEntry.getEntryDate())
                .createdAt(moodEntry.getCreatedAt())
                .updatedAt(moodEntry.getUpdatedAt())
                .suggestedActivitiesCount(
                        moodEntry.getSuggestedActivities() != null ? moodEntry.getSuggestedActivities().size() : 0)
                .build();
    }
}
