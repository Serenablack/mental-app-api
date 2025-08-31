package com.mentalapp.service;

import com.mentalapp.exception.ResourceNotFoundException;
import com.mentalapp.model.MoodEntry;
import com.mentalapp.model.User;
import com.mentalapp.dto.MoodEntryCreateRequest;
import com.mentalapp.dto.MoodEntryResponse;
import com.mentalapp.dto.MoodEntryUpdateRequest;
import com.mentalapp.mapper.MoodEntryMapper;
import com.mentalapp.repository.MoodEntryRepository;
import com.mentalapp.service.interfaces.AIActivitySuggestionService;
import com.mentalapp.service.interfaces.MoodEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoodEntryServiceImpl implements MoodEntryService {

    private final MoodEntryRepository moodEntryRepository;
    private final MoodEntryMapper moodEntryMapper;
    private final AIActivitySuggestionService aiActivitySuggestionService;

    @Override
    @Transactional
    public MoodEntryResponse createMoodEntry(MoodEntryCreateRequest request, User user) {
        MoodEntry moodEntry = moodEntryMapper.toEntity(request);
        moodEntry.setUser(user);

        // Save the mood entry first
        moodEntry = moodEntryRepository.save(moodEntry);

        // Generate and save AI suggestions
        moodEntry.setSuggestedActivities(new HashSet<>(aiActivitySuggestionService.generateSuggestions(moodEntry)));

        moodEntry = moodEntryRepository.save(moodEntry);
        return moodEntryMapper.toResponse(moodEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public MoodEntryResponse getMoodEntryById(Long id, User user) {
        MoodEntry moodEntry = moodEntryRepository.findById(id)
                .filter(entry -> entry.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Mood entry not found"));

        return moodEntryMapper.toResponse(moodEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MoodEntryResponse> getMoodEntriesByDate(User user, LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return moodEntryRepository.findByUserIdAndDate(user.getId(), startOfDay, endOfDay)
                .stream()
                .map(moodEntryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateMoodEntry(Long id, MoodEntryUpdateRequest request, User user) {
        MoodEntry moodEntry = moodEntryRepository.findById(id)
                .filter(entry -> entry.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Mood entry not found"));

        if (!moodEntry.isFromToday()) {
            throw new IllegalStateException("Cannot update mood entries from previous days");
        }

        moodEntryMapper.updateEntity(moodEntry, request);
        moodEntryRepository.save(moodEntry);
    }

    @Override
    @Transactional
    public void deleteMoodEntry(Long id, User user) {
        MoodEntry moodEntry = moodEntryRepository.findById(id)
                .filter(entry -> entry.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Mood entry not found"));

        if (!moodEntry.isFromToday()) {
            throw new IllegalStateException("Cannot delete mood entries from previous days");
        }

        moodEntryRepository.delete(moodEntry);
    }
}