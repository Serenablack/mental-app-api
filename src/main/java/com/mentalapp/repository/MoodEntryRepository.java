package com.mentalapp.repository;

import com.mentalapp.model.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MoodEntryRepository extends JpaRepository<MoodEntry, Long> {

    @Query("SELECT m FROM MoodEntry m WHERE m.user.id = :userId AND m.entryDate >= :startOfDay AND m.entryDate < :endOfDay")
    List<MoodEntry> findByUserIdAndDate(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COUNT(m) FROM MoodEntry m WHERE m.user.id = :userId AND m.entryDate >= :startOfDay AND m.entryDate < :endOfDay")
    long countTodayEntriesByUserId(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}