package com.mentalapp.repository;

import com.mentalapp.entity.SuggestedActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuggestedActivityRepository extends JpaRepository<SuggestedActivity, Long> {

    @Query("SELECT s FROM SuggestedActivity s WHERE s.user.id = :userId AND s.suggestedDate = :date ORDER BY s.createdAt ASC")
    List<SuggestedActivity> findByUserIdAndSuggestedDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query("SELECT s FROM SuggestedActivity s WHERE s.user.id = :userId AND s.suggestedDate = CURRENT_DATE ORDER BY s.createdAt ASC")
    List<SuggestedActivity> findTodayActivitiesByUserId(@Param("userId") Long userId);

    @Query("SELECT s FROM SuggestedActivity s WHERE s.user.id = :userId AND s.suggestedDate < CURRENT_DATE ORDER BY s.suggestedDate DESC, s.createdAt ASC")
    List<SuggestedActivity> findPastActivitiesByUserId(@Param("userId") Long userId);

    @Query("SELECT s FROM SuggestedActivity s WHERE s.user.id = :userId AND s.isCompleted = true ORDER BY s.completedAt DESC")
    List<SuggestedActivity> findCompletedActivitiesByUserId(@Param("userId") Long userId);

    @Query("SELECT s FROM SuggestedActivity s WHERE s.user.id = :userId AND s.isCompleted = false AND s.suggestedDate = CURRENT_DATE ORDER BY s.createdAt ASC")
    List<SuggestedActivity> findPendingTodayActivitiesByUserId(@Param("userId") Long userId);

    @Query("SELECT s FROM SuggestedActivity s WHERE s.id = :id AND s.user.id = :userId")
    Optional<SuggestedActivity> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT s FROM SuggestedActivity s WHERE s.moodEntry.id = :moodEntryId ORDER BY s.createdAt ASC")
    List<SuggestedActivity> findByMoodEntryId(@Param("moodEntryId") Long moodEntryId);

    @Query("SELECT COUNT(s) FROM SuggestedActivity s WHERE s.user.id = :userId AND s.isCompleted = true AND s.suggestedDate = CURRENT_DATE")
    long countCompletedTodayByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(s) FROM SuggestedActivity s WHERE s.user.id = :userId AND s.suggestedDate = CURRENT_DATE")
    long countTodayActivitiesByUserId(@Param("userId") Long userId);

    @Query("SELECT s FROM SuggestedActivity s WHERE s.user.id = :userId AND s.category = :category ORDER BY s.createdAt DESC")
    List<SuggestedActivity> findByUserIdAndCategory(@Param("userId") Long userId,
            @Param("category") SuggestedActivity.ActivityCategory category);
}
