package com.mentalapp.service.interfaces;

import com.mentalapp.model.MoodEntry;
import com.mentalapp.model.User;
import com.mentalapp.dto.MoodEntryCreateRequest;
import com.mentalapp.dto.MoodEntryResponse;
import com.mentalapp.dto.MoodEntryUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface MoodEntryService {

    MoodEntryResponse createMoodEntry(MoodEntryCreateRequest request, User user);

    MoodEntryResponse getMoodEntryById(Long id, User user);

    List<MoodEntryResponse> getMoodEntriesByDate(User user, LocalDateTime date);

    void updateMoodEntry(Long id, MoodEntryUpdateRequest request, User user);

    void deleteMoodEntry(Long id, User user);
}