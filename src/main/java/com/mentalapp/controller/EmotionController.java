package com.mentalapp.controller;

import com.mentalapp.model.Emotion;
import com.mentalapp.dto.MoodEntryResponse;
import com.mentalapp.service.interfaces.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emotions")
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    @GetMapping
    public ResponseEntity<List<Emotion>> getAllEmotions() {
        return ResponseEntity.ok(emotionService.getAllEmotions());
    }

    @GetMapping("/root")
    public ResponseEntity<List<Emotion>> getRootEmotions() {
        return ResponseEntity.ok(emotionService.getRootEmotions());
    }

    @GetMapping("/taxonomy")
    public ResponseEntity<List<Emotion>> getEmotionTaxonomy() {
        return ResponseEntity.ok(emotionService.getEmotionTaxonomy());
    }

    @GetMapping("/dropdown")
    public ResponseEntity<List<MoodEntryResponse.EmotionResponse>> getEmotionsForDropdown() {
        return ResponseEntity.ok(emotionService.getEmotionsForDropdown());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emotion> getEmotionById(@PathVariable Long id) {
        return emotionService.getEmotionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/key/{key}")
    public ResponseEntity<Emotion> getEmotionByKey(@PathVariable String key) {
        return emotionService.getEmotionByKey(key)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/parent/{parentKey}")
    public ResponseEntity<List<Emotion>> getEmotionsByParentKey(@PathVariable String parentKey) {
        return ResponseEntity.ok(emotionService.getEmotionsByParentKey(parentKey));
    }
}