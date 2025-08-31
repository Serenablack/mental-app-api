package com.mentalapp.service;

import com.mentalapp.entity.Emotion;
import com.mentalapp.repository.EmotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmotionService {
    
    private final EmotionRepository emotionRepository;
    
    /**
     * Get all emotions
     */
    @Transactional(readOnly = true)
    public List<Emotion> getAllEmotions() {
        return emotionRepository.findAll();
    }
    
    /**
     * Get emotion by key
     */
    @Transactional(readOnly = true)
    public Optional<Emotion> getEmotionByKey(String key) {
        return emotionRepository.findByKey(key);
    }
    
    /**
     * Get all root emotions (main categories for dropdown)
     */
    @Transactional(readOnly = true)
    public List<Emotion> getRootEmotions() {
        return emotionRepository.findRootEmotions();
    }
    
    /**
     * Get emotions by parent key (sub-emotions for dropdown)
     */
    @Transactional(readOnly = true)
    public List<Emotion> getEmotionsByParentKey(String parentKey) {
        return emotionRepository.findByParentKey(parentKey);
    }
    
    /**
     * Get full emotion wheel taxonomy with hierarchy (for nested dropdowns)
     */
    @Transactional(readOnly = true)
    public List<Emotion> getEmotionWheelTaxonomy() {
        return emotionRepository.findRootEmotionsWithChildren();
    }
    
    /**
     * Search emotions by label
     */
    @Transactional(readOnly = true)
    public List<Emotion> searchEmotionsByLabel(String searchTerm) {
        return emotionRepository.findByLabelContainingIgnoreCase(searchTerm);
    }
    
    /**
     * Get all emotions formatted for dropdown (both root and children)
     */
    @Transactional(readOnly = true)
    public List<Emotion> getAllEmotionsForDropdown() {
        return emotionRepository.findAllForDropdown();
    }
    
    /**
     * Get emotions by category
     */
    @Transactional(readOnly = true)
    public List<Emotion> getEmotionsByCategory(String categoryKey) {
        return emotionRepository.findByCategory(categoryKey);
    }
    
    /**
     * Get sub-emotions only (for two-level dropdown)
     */
    @Transactional(readOnly = true)
    public List<Emotion> getSubEmotions() {
        return emotionRepository.findSubEmotions();
    }
    
    /**
     * Get sub-emotions ordered by parent (for grouped dropdown)
     */
    @Transactional(readOnly = true)
    public List<Emotion> getSubEmotionsOrderedByParent() {
        return emotionRepository.findSubEmotionsOrderedByParent();
    }
    
    /**
     * Check if emotion exists
     */
    @Transactional(readOnly = true)
    public boolean emotionExists(String key) {
        return emotionRepository.existsByKey(key);
    }
    
    /**
     * Get emotions for specific dropdown scenarios
     */
    @Transactional(readOnly = true)
    public List<Emotion> getEmotionsForDropdown(String type) {
        switch (type.toLowerCase()) {
            case "main":
                return getRootEmotions();
            case "sub":
                return getSubEmotions();
            case "all":
                return getAllEmotionsForDropdown();
            case "hierarchy":
                return getEmotionWheelTaxonomy();
            default:
                return getAllEmotions();
        }
    }
}




