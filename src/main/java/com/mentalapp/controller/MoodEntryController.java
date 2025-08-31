package com.mentalapp.controller;

import com.mentalapp.model.MoodEntry;
import com.mentalapp.model.User;
import com.mentalapp.dto.MoodEntryCreateRequest;
import com.mentalapp.dto.MoodEntryResponse;
import com.mentalapp.dto.MoodEntryUpdateRequest;
import com.mentalapp.service.interfaces.MoodEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/mood-entries")
@RequiredArgsConstructor
public class MoodEntryController {

    private final MoodEntryService moodEntryService;

    @PostMapping
    public ResponseEntity<MoodEntryResponse> createMoodEntry(
            @Valid @RequestBody MoodEntryCreateRequest request,
            @AuthenticationPrincipal User user) {
        MoodEntryResponse response = moodEntryService.createMoodEntry(request, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoodEntryResponse> getMoodEntryById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(moodEntryService.getMoodEntryById(id, user));
    }

    @GetMapping
    public ResponseEntity<List<MoodEntryResponse>> getMoodEntriesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(moodEntryService.getMoodEntriesByDate(user, date));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMoodEntry(
            @PathVariable Long id,
            @Valid @RequestBody MoodEntryUpdateRequest request,
            @AuthenticationPrincipal User user) {
        moodEntryService.updateMoodEntry(id, request, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoodEntry(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        moodEntryService.deleteMoodEntry(id, user);
        return ResponseEntity.ok().build();
    }
}