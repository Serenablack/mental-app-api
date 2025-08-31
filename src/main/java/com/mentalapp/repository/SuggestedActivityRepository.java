package com.mentalapp.repository;

import com.mentalapp.model.SuggestedActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SuggestedActivityRepository extends JpaRepository<SuggestedActivity, Long> {

    @Query("SELECT sa FROM SuggestedActivity sa WHERE sa.moodEntry.user.id = :userId AND sa.moodEntry.entryDate >= :startOfDay AND sa.moodEntry.entryDate < :endOfDay")
    List<SuggestedActivity> findByUserIdAndDate(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT sa FROM SuggestedActivity sa WHERE sa.moodEntry.user.id = :userId AND sa.activityType = :activityType")
    List<SuggestedActivity> findByUserIdAndType(@Param("userId") Long userId, @Param("activityType") String activityType);
}