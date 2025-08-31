package com.mentalapp.repository;

import com.mentalapp.model.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {

    Optional<Emotion> findByKey(String key);

    @Query("SELECT e FROM Emotion e WHERE e.parent IS NULL")
    List<Emotion> findRootEmotions();

    @Query("SELECT e FROM Emotion e WHERE e.parent.key = :parentKey")
    List<Emotion> findByParentKey(@Param("parentKey") String parentKey);

    @Query("SELECT DISTINCT e FROM Emotion e LEFT JOIN FETCH e.children WHERE e.parent IS NULL")
    List<Emotion> findRootEmotionsWithChildren();

    boolean existsByKey(String key);
}