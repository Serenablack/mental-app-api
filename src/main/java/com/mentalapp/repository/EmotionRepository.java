package com.mentalapp.repository;

import com.mentalapp.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    
    /**
     * Find emotion by its unique key
     */
    Optional<Emotion> findByKey(String key);
    
    /**
     * Find all root emotions (main categories for dropdown)
     */
    @Query("SELECT e FROM Emotion e WHERE e.parent IS NULL ORDER BY e.label")
    List<Emotion> findRootEmotions();
    
    /**
     * Find all emotions by parent key (sub-emotions for dropdown)
     */
    @Query("SELECT e FROM Emotion e WHERE e.parent.key = :parentKey ORDER BY e.label")
    List<Emotion> findByParentKey(@Param("parentKey") String parentKey);
    
    /**
     * Find all emotions with their children (for building the full dropdown hierarchy)
     */
    @Query("SELECT DISTINCT e FROM Emotion e LEFT JOIN FETCH e.children WHERE e.parent IS NULL ORDER BY e.label")
    List<Emotion> findRootEmotionsWithChildren();
    
    /**
     * Find emotions by partial label match (case-insensitive search)
     */
    @Query("SELECT e FROM Emotion e WHERE LOWER(e.label) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY e.label")
    List<Emotion> findByLabelContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    
    /**
     * Find all emotions for dropdown (both root and children)
     */
    @Query("SELECT e FROM Emotion e ORDER BY CASE WHEN e.parent IS NULL THEN 0 ELSE 1 END, e.parent.key, e.label")
    List<Emotion> findAllForDropdown();
    
    /**
     * Find emotions by category (for filtering dropdowns)
     */
    @Query("SELECT e FROM Emotion e WHERE e.parent.key = :categoryKey OR e.key = :categoryKey ORDER BY e.label")
    List<Emotion> findByCategory(@Param("categoryKey") String categoryKey);
    
    /**
     * Check if emotion exists by key
     */
    boolean existsByKey(String key);
    
    /**
     * Find emotions that are direct children (for two-level dropdown)
     */
    @Query("SELECT e FROM Emotion e WHERE e.parent IS NOT NULL ORDER BY e.parent.key, e.label")
    List<Emotion> findSubEmotions();
    
    /**
     * Find emotions grouped by parent for dropdown structure
     */
    @Query("SELECT e FROM Emotion e WHERE e.parent IS NOT NULL ORDER BY e.parent.label, e.label")
    List<Emotion> findSubEmotionsOrderedByParent();
}



