package com.mentalapp.service.interfaces;

import com.mentalapp.model.Emotion;
import com.mentalapp.dto.MoodEntryResponse;

import java.util.List;
import java.util.Optional;

public interface EmotionService {

    List<Emotion> getAllEmotions();

    Optional<Emotion> getEmotionById(Long id);

    Optional<Emotion> getEmotionByKey(String key);

    List<Emotion> getRootEmotions();

    List<Emotion> getEmotionsByParentKey(String parentKey);

    List<Emotion> getEmotionTaxonomy();

    List<MoodEntryResponse.EmotionResponse> getEmotionsForDropdown();
}