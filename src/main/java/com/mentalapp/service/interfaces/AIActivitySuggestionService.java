package com.mentalapp.service.interfaces;

import com.mentalapp.model.MoodEntry;
import com.mentalapp.model.SuggestedActivity;

import java.util.List;

public interface AIActivitySuggestionService {

    List<SuggestedActivity> generateSuggestions(MoodEntry moodEntry);

    String generateGeminiPrompt(MoodEntry moodEntry);

    List<SuggestedActivity> processGeminiResponse(String geminiResponse, MoodEntry moodEntry);
}