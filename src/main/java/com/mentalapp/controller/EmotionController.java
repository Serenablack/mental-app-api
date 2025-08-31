package com.mentalapp.controller;

import com.mentalapp.entity.Emotion;
import com.mentalapp.service.EmotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/emotions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class EmotionController {
    
    private final EmotionService emotionService;
    
    /**
     * Get all emotions
     */
    @GetMapping
    public ResponseEntity<List<Emotion>> getAllEmotions() {
        log.info("Fetching all emotions");
        List<Emotion> emotions = emotionService.getAllEmotions();
        return ResponseEntity.ok(emotions);
    }
    
    /**
     * Get emotion by key
     */
    @GetMapping("/{key}")
    public ResponseEntity<Emotion> getEmotionByKey(@PathVariable String key) {
        log.info("Fetching emotion with key: {}", key);
        Optional<Emotion> emotion = emotionService.getEmotionByKey(key);
        return emotion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get root emotions (main categories for dropdown)
     */
    @GetMapping("/root")
    public ResponseEntity<List<Emotion>> getRootEmotions() {
        log.info("Fetching root emotions for dropdown");
        List<Emotion> emotions = emotionService.getRootEmotions();
        return ResponseEntity.ok(emotions);
    }
    
    /**
     * Get emotions by parent key (sub-emotions for dropdown)
     */
    @GetMapping("/parent/{parentKey}")
    public ResponseEntity<List<Emotion>> getEmotionsByParentKey(@PathVariable String parentKey) {
        log.info("Fetching sub-emotions for parent key: {}", parentKey);
        List<Emotion> emotions = emotionService.getEmotionsByParentKey(parentKey);
        return ResponseEntity.ok(emotions);
    }
    
    /**
     * Get full emotion wheel taxonomy with hierarchy (for nested dropdowns)
     */
    @GetMapping("/taxonomy")
    public ResponseEntity<List<Emotion>> getEmotionWheelTaxonomy() {
        log.info("Fetching emotion wheel taxonomy for nested dropdown");
        List<Emotion> emotions = emotionService.getEmotionWheelTaxonomy();
        return ResponseEntity.ok(emotions);
    }
    
    /**
     * Search emotions by label
     */
    @GetMapping("/search")
    public ResponseEntity<List<Emotion>> searchEmotions(@RequestParam String q) {
        log.info("Searching emotions with query: {}", q);
        List<Emotion> emotions = emotionService.searchEmotionsByLabel(q);
        return ResponseEntity.ok(emotions);
    }
    
    /**
     * Get all emotions formatted for dropdown (both root and children)
     */
    @GetMapping("/dropdown")
    public ResponseEntity<List<Emotion>> getEmotionsForDropdown() {
        log.info("Fetching emotions for dropdown");
        List<Emotion> emotions = emotionService.getAllEmotionsForDropdown();
        return ResponseEntity.ok(emotions);
    }
    
    /**
     * Get emotions for specific dropdown type
     */
    @GetMapping("/dropdown/{type}")
    public ResponseEntity<List<Emotion>> getEmotionsForDropdownType(@PathVariable String type) {
        log.info("Fetching emotions for dropdown type: {}", type);
        List<Emotion> emotions = emotionService.getEmotionsForDropdown(type);
        return ResponseEntity.ok(emotions);
    }
    
    /**
     * Get emotions by category
     */
    @GetMapping("/category/{categoryKey}")
    public ResponseEntity<List<Emotion>> getEmotionsByCategory(@PathVariable String categoryKey) {
        log.info("Fetching emotions for category: {}", categoryKey);
        List<Emotion> emotions = emotionService.getEmotionsByCategory(categoryKey);
        return ResponseEntity.ok(emotions);
    }
    
    /**
     * Get sub-emotions only (for two-level dropdown)
     */
    @GetMapping("/sub")
    public ResponseEntity<List<Emotion>> getSubEmotions() {
        log.info("Fetching sub-emotions for two-level dropdown");
        List<Emotion> emotions = emotionService.getSubEmotions();
        return ResponseEntity.ok(emotions);
    }
    
    /**
     * Get sub-emotions ordered by parent (for grouped dropdown)
     */
    @GetMapping("/sub/grouped")
    public ResponseEntity<List<Emotion>> getSubEmotionsGrouped() {
        log.info("Fetching sub-emotions grouped by parent");
        List<Emotion> emotions = emotionService.getSubEmotionsOrderedByParent();
        return ResponseEntity.ok(emotions);
    }
    
    /**
     * Check if emotion exists
     */
    @GetMapping("/exists/{key}")
    public ResponseEntity<Boolean> emotionExists(@PathVariable String key) {
        log.info("Checking if emotion exists with key: {}", key);
        boolean exists = emotionService.emotionExists(key);
        return ResponseEntity.ok(exists);
    }
}




