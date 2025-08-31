package com.mentalapp.repository;

import com.mentalapp.entity.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MoodEntryRepository extends JpaRepository<MoodEntry, Long> {

    @Query("SELECT m FROM MoodEntry m WHERE m.user.id = :userId ORDER BY m.entryDate DESC")
    List<MoodEntry> findByUserIdOrderByEntryDateDesc(@Param("userId") Long userId);

    @Query("SELECT m FROM MoodEntry m WHERE m.user.id = :userId AND m.entryDate >= :startOfDay AND m.entryDate < :endOfDay ORDER BY m.entryDate DESC")
    List<MoodEntry> findByUserIdAndEntryDate(@Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT m FROM MoodEntry m WHERE m.user.id = :userId AND m.entryDate >= :startOfDay AND m.entryDate < :endOfDay ORDER BY m.entryDate DESC")
    List<MoodEntry> findTodayMoodEntriesByUserId(@Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT m FROM MoodEntry m WHERE m.user.id = :userId AND m.entryDate >= :startDate AND m.entryDate <= :endDate ORDER BY m.entryDate DESC")
    List<MoodEntry> findByUserIdAndDateRange(@Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT m FROM MoodEntry m WHERE m.id = :id AND m.user.id = :userId")
    Optional<MoodEntry> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT COUNT(m) FROM MoodEntry m WHERE m.user.id = :userId AND m.entryDate >= :startOfDay AND m.entryDate < :endOfDay")
    long countTodayEntriesByUserId(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT AVG(m.energyLevel) FROM MoodEntry m WHERE m.user.id = :userId AND m.entryDate >= :startDate")
    Double getAverageEnergyLevelSince(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT m FROM MoodEntry m WHERE m.user.id = :userId AND m.entryDate >= :startOfDay AND m.entryDate < :endOfDay AND m.createdAt = m.updatedAt")
    List<MoodEntry> findTodayEditableMoodEntries(@Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
