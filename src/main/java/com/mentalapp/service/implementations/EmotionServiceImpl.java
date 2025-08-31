package com.mentalapp.service.implementations;

import com.mentalapp.model.Emotion;
import com.mentalapp.dto.MoodEntryResponse;
import com.mentalapp.mapper.EmotionMapper;
import com.mentalapp.repository.EmotionRepository;
import com.mentalapp.service.interfaces.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmotionServiceImpl implements EmotionService {

    private final EmotionRepository emotionRepository;
    private final EmotionMapper emotionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Emotion> getAllEmotions() {
        return emotionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Emotion> getEmotionById(Long id) {
        return emotionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Emotion> getEmotionByKey(String key) {
        return emotionRepository.findByKey(key);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Emotion> getRootEmotions() {
        return emotionRepository.findRootEmotions();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Emotion> getEmotionsByParentKey(String parentKey) {
        return emotionRepository.findByParentKey(parentKey);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Emotion> getEmotionTaxonomy() {
        return emotionRepository.findRootEmotionsWithChildren();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MoodEntryResponse.EmotionResponse> getEmotionsForDropdown() {
        return emotionRepository.findAll().stream()
                .map(emotionMapper::toResponse)
                .collect(Collectors.toList());
    }
}