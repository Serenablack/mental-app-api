package com.mentalapp.service;

import com.mentalapp.entity.MoodEntry;
import com.mentalapp.entity.SuggestedActivity;
import com.mentalapp.entity.User;
import com.mentalapp.repository.MoodEntryRepository;
import com.mentalapp.repository.SuggestedActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MoodEntryService {

    private final MoodEntryRepository moodEntryRepository;
    private final SuggestedActivityRepository suggestedActivityRepository;
    private final AIActivitySuggestionService aiActivitySuggestionService;

    /**
     * Create a new mood entry and generate AI suggestions
     */
    public MoodEntry createMoodEntry(MoodEntry moodEntry, User user) {
        log.info("Creating mood entry for user: {}", user.getId());

        moodEntry.setUser(user);
        MoodEntry savedEntry = moodEntryRepository.save(moodEntry);

        // Generate AI activity suggestions
        List<SuggestedActivity> suggestions = aiActivitySuggestionService.generateActivitySuggestions(savedEntry);

        // Save all suggestions
        if (suggestions != null && !suggestions.isEmpty()) {
            suggestedActivityRepository.saveAll(suggestions);
            savedEntry.setSuggestedActivities(suggestions);
        }

        log.info("Created mood entry with {} AI suggestions", suggestions != null ? suggestions.size() : 0);
        return savedEntry;
    }

    /**
     * Update mood entry (only allowed on the same day it was created)
     */
    public Optional<MoodEntry> updateMoodEntry(Long id, MoodEntry updatedEntry, User user) {
        log.info("Updating mood entry: {} for user: {}", id, user.getId());

        Optional<MoodEntry> existingEntryOpt = moodEntryRepository.findByIdAndUserId(id, user.getId());

        if (existingEntryOpt.isEmpty()) {
            log.warn("Mood entry not found or user not authorized: {}", id);
            return Optional.empty();
        }

        MoodEntry existingEntry = existingEntryOpt.get();

        // Check if entry can be edited (same day as creation)
        if (!isSameDay(existingEntry.getCreatedAt(), LocalDateTime.now())) {
            log.warn("Cannot edit mood entry from previous day: {}", id);
            return Optional.empty();
        }

        // Update fields
        existingEntry.setEmotionKeys(updatedEntry.getEmotionKeys());
        existingEntry.setLocation(updatedEntry.getLocation());
        existingEntry.setEnvironment(updatedEntry.getEnvironment());
        existingEntry.setDescription(updatedEntry.getDescription());
        existingEntry.setEnergyLevel(updatedEntry.getEnergyLevel());
        existingEntry.setIsVoiceInput(updatedEntry.getIsVoiceInput());

        MoodEntry savedEntry = moodEntryRepository.save(existingEntry);

        // Regenerate AI suggestions if emotions or energy changed significantly
        regenerateAISuggestionsIfNeeded(savedEntry, updatedEntry);

        return Optional.of(savedEntry);
    }

    /**
     * Delete mood entry (only allowed on the same day it was created)
     */
    public boolean deleteMoodEntry(Long id, User user) {
        log.info("Deleting mood entry: {} for user: {}", id, user.getId());

        Optional<MoodEntry> entryOpt = moodEntryRepository.findByIdAndUserId(id, user.getId());

        if (entryOpt.isEmpty()) {
            log.warn("Mood entry not found or user not authorized: {}", id);
            return false;
        }

        MoodEntry entry = entryOpt.get();

        // Check if entry can be deleted (same day as creation)
        if (!isSameDay(entry.getCreatedAt(), LocalDateTime.now())) {
            log.warn("Cannot delete mood entry from previous day: {}", id);
            return false;
        }

        moodEntryRepository.delete(entry);
        log.info("Successfully deleted mood entry: {}", id);
        return true;
    }

    /**
     * Get all mood entries for a user
     */
    @Transactional(readOnly = true)
    public List<MoodEntry> getUserMoodEntries(User user) {
        return moodEntryRepository.findByUserIdOrderByEntryDateDesc(user.getId());
    }

    /**
     * Get today's mood entries for a user
     */
    @Transactional(readOnly = true)
    public List<MoodEntry> getTodayMoodEntries(User user) {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        return moodEntryRepository.findTodayMoodEntriesByUserId(user.getId(), startOfDay, endOfDay);
    }

    /**
     * Get mood entries for a specific date
     */
    @Transactional(readOnly = true)
    public List<MoodEntry> getMoodEntriesByDate(User user, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return moodEntryRepository.findByUserIdAndEntryDate(user.getId(), startOfDay, endOfDay);
    }

    /**
     * Get mood entries within date range
     */
    @Transactional(readOnly = true)
    public List<MoodEntry> getMoodEntriesInRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return moodEntryRepository.findByUserIdAndDateRange(user.getId(), startDate, endDate);
    }

    /**
     * Get today's editable mood entries
     */
    @Transactional(readOnly = true)
    public List<MoodEntry> getTodayEditableMoodEntries(User user) {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        return moodEntryRepository.findTodayEditableMoodEntries(user.getId(), startOfDay, endOfDay);
    }

    /**
     * Get mood entry by ID if user owns it
     */
    @Transactional(readOnly = true)
    public Optional<MoodEntry> getMoodEntryById(Long id, User user) {
        return moodEntryRepository.findByIdAndUserId(id, user.getId());
    }

    /**
     * Get user's mood statistics
     */
    @Transactional(readOnly = true)
    public MoodStatistics getUserMoodStatistics(User user) {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        long todayEntries = moodEntryRepository.countTodayEntriesByUserId(user.getId(), startOfDay, endOfDay);
        Double avgEnergyWeek = moodEntryRepository.getAverageEnergyLevelSince(user.getId(),
                LocalDateTime.now().minusWeeks(1));
        Double avgEnergyMonth = moodEntryRepository.getAverageEnergyLevelSince(user.getId(),
                LocalDateTime.now().minusMonths(1));

        return MoodStatistics.builder()
                .todayEntries(todayEntries)
                .averageEnergyLevelWeek(avgEnergyWeek != null ? avgEnergyWeek : 0.0)
                .averageEnergyLevelMonth(avgEnergyMonth != null ? avgEnergyMonth : 0.0)
                .build();
    }

    private boolean isSameDay(LocalDateTime date1, LocalDateTime date2) {
        return date1.toLocalDate().equals(date2.toLocalDate());
    }

    private void regenerateAISuggestionsIfNeeded(MoodEntry existingEntry, MoodEntry updatedEntry) {
        // Simple check - if emotions or energy level changed significantly, regenerate
        boolean shouldRegenerate = !existingEntry.getEmotionKeys().equals(updatedEntry.getEmotionKeys()) ||
                Math.abs(existingEntry.getEnergyLevel() - updatedEntry.getEnergyLevel()) >= 2;

        if (shouldRegenerate) {
            log.info("Regenerating AI suggestions for updated mood entry: {}", existingEntry.getId());

            // Remove old suggestions for today
            List<SuggestedActivity> oldSuggestions = suggestedActivityRepository
                    .findByMoodEntryId(existingEntry.getId());
            suggestedActivityRepository.deleteAll(oldSuggestions);

            // Generate new suggestions
            List<SuggestedActivity> newSuggestions = aiActivitySuggestionService
                    .generateActivitySuggestions(existingEntry);

            if (newSuggestions != null && !newSuggestions.isEmpty()) {
                suggestedActivityRepository.saveAll(newSuggestions);
            }
        }
    }

    @lombok.Data
    @lombok.Builder
    public static class MoodStatistics {
        private long todayEntries;
        private double averageEnergyLevelWeek;
        private double averageEnergyLevelMonth;
    }
}
