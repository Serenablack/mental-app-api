package com.mentalapp.service;

import com.mentalapp.entity.SuggestedActivity;
import com.mentalapp.entity.User;
import com.mentalapp.repository.SuggestedActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SuggestedActivityService {

    private final SuggestedActivityRepository suggestedActivityRepository;

    /**
     * Mark activity as completed
     */
    public Optional<SuggestedActivity> completeActivity(Long activityId, User user) {
        log.info("Marking activity as completed: {} for user: {}", activityId, user.getId());

        Optional<SuggestedActivity> activityOpt = suggestedActivityRepository
                .findByIdAndUserId(activityId, user.getId());

        if (activityOpt.isEmpty()) {
            log.warn("Activity not found or user not authorized: {}", activityId);
            return Optional.empty();
        }

        SuggestedActivity activity = activityOpt.get();
        activity.setIsCompleted(true);

        SuggestedActivity savedActivity = suggestedActivityRepository.save(activity);
        log.info("Successfully completed activity: {}", activityId);

        return Optional.of(savedActivity);
    }

    /**
     * Mark activity as incomplete (undo completion)
     */
    public Optional<SuggestedActivity> uncompleteActivity(Long activityId, User user) {
        log.info("Marking activity as incomplete: {} for user: {}", activityId, user.getId());

        Optional<SuggestedActivity> activityOpt = suggestedActivityRepository
                .findByIdAndUserId(activityId, user.getId());

        if (activityOpt.isEmpty()) {
            log.warn("Activity not found or user not authorized: {}", activityId);
            return Optional.empty();
        }

        SuggestedActivity activity = activityOpt.get();
        activity.setIsCompleted(false);
        activity.setCompletedAt(null);

        SuggestedActivity savedActivity = suggestedActivityRepository.save(activity);
        log.info("Successfully uncompleted activity: {}", activityId);

        return Optional.of(savedActivity);
    }

    /**
     * Get today's activities for user
     */
    @Transactional(readOnly = true)
    public List<SuggestedActivity> getTodayActivities(User user) {
        return suggestedActivityRepository.findTodayActivitiesByUserId(user.getId());
    }

    /**
     * Get pending (incomplete) today's activities for user
     */
    @Transactional(readOnly = true)
    public List<SuggestedActivity> getPendingTodayActivities(User user) {
        return suggestedActivityRepository.findPendingTodayActivitiesByUserId(user.getId());
    }

    /**
     * Get activities for a specific date
     */
    @Transactional(readOnly = true)
    public List<SuggestedActivity> getActivitiesByDate(User user, LocalDate date) {
        return suggestedActivityRepository.findByUserIdAndSuggestedDate(user.getId(), date);
    }

    /**
     * Get past activities (history)
     */
    @Transactional(readOnly = true)
    public List<SuggestedActivity> getPastActivities(User user) {
        return suggestedActivityRepository.findPastActivitiesByUserId(user.getId());
    }

    /**
     * Get all completed activities for user
     */
    @Transactional(readOnly = true)
    public List<SuggestedActivity> getCompletedActivities(User user) {
        return suggestedActivityRepository.findCompletedActivitiesByUserId(user.getId());
    }

    /**
     * Get activities by category
     */
    @Transactional(readOnly = true)
    public List<SuggestedActivity> getActivitiesByCategory(User user, SuggestedActivity.ActivityCategory category) {
        return suggestedActivityRepository.findByUserIdAndCategory(user.getId(), category);
    }

    /**
     * Get activity by ID if user owns it
     */
    @Transactional(readOnly = true)
    public Optional<SuggestedActivity> getActivityById(Long activityId, User user) {
        return suggestedActivityRepository.findByIdAndUserId(activityId, user.getId());
    }

    /**
     * Get user's activity statistics
     */
    @Transactional(readOnly = true)
    public ActivityStatistics getUserActivityStatistics(User user) {
        long todayTotal = suggestedActivityRepository.countTodayActivitiesByUserId(user.getId());
        long todayCompleted = suggestedActivityRepository.countCompletedTodayByUserId(user.getId());

        return ActivityStatistics.builder()
                .todayTotal(todayTotal)
                .todayCompleted(todayCompleted)
                .todayPending(todayTotal - todayCompleted)
                .completionRate(todayTotal > 0 ? (double) todayCompleted / todayTotal * 100 : 0.0)
                .build();
    }

    /**
     * Delete activity (mainly for admin purposes or cleanup)
     */
    public boolean deleteActivity(Long activityId, User user) {
        log.info("Deleting activity: {} for user: {}", activityId, user.getId());

        Optional<SuggestedActivity> activityOpt = suggestedActivityRepository
                .findByIdAndUserId(activityId, user.getId());

        if (activityOpt.isEmpty()) {
            log.warn("Activity not found or user not authorized: {}", activityId);
            return false;
        }

        suggestedActivityRepository.delete(activityOpt.get());
        log.info("Successfully deleted activity: {}", activityId);
        return true;
    }

    @lombok.Data
    @lombok.Builder
    public static class ActivityStatistics {
        private long todayTotal;
        private long todayCompleted;
        private long todayPending;
        private double completionRate;
    }
}
