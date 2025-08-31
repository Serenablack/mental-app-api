package com.mentalapp.controller;

import com.mentalapp.entity.SuggestedActivity;
import com.mentalapp.entity.User;
import com.mentalapp.service.SuggestedActivityService;
import com.mentalapp.dto.SuggestedActivityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SuggestedActivityController {

    private final SuggestedActivityService suggestedActivityService;

    @PostMapping("/{id}/complete")
    public ResponseEntity<SuggestedActivityResponse> completeActivity(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {

        log.info("Completing activity: {} for user: {}", id, user.getId());

        Optional<SuggestedActivity> result = suggestedActivityService.completeActivity(id, user);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToResponse(result.get()));
    }

    @PostMapping("/{id}/uncomplete")
    public ResponseEntity<SuggestedActivityResponse> uncompleteActivity(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {

        log.info("Uncompleting activity: {} for user: {}", id, user.getId());

        Optional<SuggestedActivity> result = suggestedActivityService.uncompleteActivity(id, user);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToResponse(result.get()));
    }

    @GetMapping("/today")
    public ResponseEntity<List<SuggestedActivityResponse>> getTodayActivities(
            @RequestAttribute("user") User user) {

        List<SuggestedActivity> activities = suggestedActivityService.getTodayActivities(user);
        List<SuggestedActivityResponse> responses = activities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/today/pending")
    public ResponseEntity<List<SuggestedActivityResponse>> getPendingTodayActivities(
            @RequestAttribute("user") User user) {

        List<SuggestedActivity> activities = suggestedActivityService.getPendingTodayActivities(user);
        List<SuggestedActivityResponse> responses = activities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<SuggestedActivityResponse>> getActivitiesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestAttribute("user") User user) {

        List<SuggestedActivity> activities = suggestedActivityService.getActivitiesByDate(user, date);
        List<SuggestedActivityResponse> responses = activities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/history")
    public ResponseEntity<List<SuggestedActivityResponse>> getPastActivities(
            @RequestAttribute("user") User user) {

        List<SuggestedActivity> activities = suggestedActivityService.getPastActivities(user);
        List<SuggestedActivityResponse> responses = activities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<SuggestedActivityResponse>> getCompletedActivities(
            @RequestAttribute("user") User user) {

        List<SuggestedActivity> activities = suggestedActivityService.getCompletedActivities(user);
        List<SuggestedActivityResponse> responses = activities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SuggestedActivityResponse>> getActivitiesByCategory(
            @PathVariable SuggestedActivity.ActivityCategory category,
            @RequestAttribute("user") User user) {

        List<SuggestedActivity> activities = suggestedActivityService.getActivitiesByCategory(user, category);
        List<SuggestedActivityResponse> responses = activities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuggestedActivityResponse> getActivityById(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {

        Optional<SuggestedActivity> activity = suggestedActivityService.getActivityById(id, user);

        if (activity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToResponse(activity.get()));
    }

    @GetMapping("/statistics")
    public ResponseEntity<SuggestedActivityService.ActivityStatistics> getActivityStatistics(
            @RequestAttribute("user") User user) {

        SuggestedActivityService.ActivityStatistics statistics = suggestedActivityService
                .getUserActivityStatistics(user);
        return ResponseEntity.ok(statistics);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(
            @PathVariable Long id,
            @RequestAttribute("user") User user) {

        log.info("Deleting activity: {} for user: {}", id, user.getId());

        boolean deleted = suggestedActivityService.deleteActivity(id, user);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private SuggestedActivityResponse convertToResponse(SuggestedActivity activity) {
        return SuggestedActivityResponse.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .description(activity.getDescription())
                .category(activity.getCategory())
                .estimatedDurationMinutes(activity.getEstimatedDurationMinutes())
                .difficultyLevel(activity.getDifficultyLevel())
                .isCompleted(activity.getIsCompleted())
                .completedAt(activity.getCompletedAt())
                .suggestedDate(activity.getSuggestedDate())
                .createdAt(activity.getCreatedAt())
                .updatedAt(activity.getUpdatedAt())
                .moodEntryId(activity.getMoodEntry() != null ? activity.getMoodEntry().getId() : null)
                .build();
    }
}
