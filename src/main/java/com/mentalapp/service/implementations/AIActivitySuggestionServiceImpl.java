package com.mentalapp.service.implementations;

import com.mentalapp.model.Emotion;
import com.mentalapp.model.MoodEntry;
import com.mentalapp.model.SuggestedActivity;
import com.mentalapp.service.interfaces.AIActivitySuggestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIActivitySuggestionServiceImpl implements AIActivitySuggestionService {

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent}")
    private String geminiApiUrl;

    @Override
    public List<SuggestedActivity> generateSuggestions(MoodEntry moodEntry) {
        log.info("Generating AI suggestions for mood entry: {}", moodEntry.getId());

        try {
            // First try Gemini API
            if (geminiApiKey != null && !geminiApiKey.isEmpty()) {
                return generateSuggestionsWithGemini(moodEntry);
            } else {
                log.warn("Gemini API key not configured, using fallback suggestions");
                return generateFallbackSuggestions(moodEntry);
            }
        } catch (Exception e) {
            log.error("Error generating AI suggestions for mood entry: {}", moodEntry.getId(), e);
            return generateFallbackSuggestions(moodEntry);
        }
    }

    private List<SuggestedActivity> generateSuggestionsWithGemini(MoodEntry moodEntry) {
        try {
            String prompt = generateGeminiPrompt(moodEntry);
            String geminiResponse = callGeminiAPI(prompt);
            return processGeminiResponse(geminiResponse, moodEntry);
        } catch (Exception e) {
            log.error("Error calling Gemini API, falling back to rule-based suggestions", e);
            return generateRuleBasedSuggestions(moodEntry);
        }
    }

    @Override
    public String generateGeminiPrompt(MoodEntry moodEntry) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(
                "Based on the following mood entry, suggest 3 personalized activities that would be beneficial for mental health and well-being. ");
        prompt.append("Consider the user's emotions, energy level, environment, and passion. ");
        prompt.append("Each activity should be specific, actionable, and appropriate for their current state.\n\n");

        prompt.append("Mood Entry Details:\n");
        prompt.append("- Emotions: ").append(getEmotionLabels(moodEntry.getEmotions())).append("\n");
        prompt.append("- Energy Level: ").append(moodEntry.getEnergyLevel()).append("/5\n");
        prompt.append("- Environment: ").append(moodEntry.getComfortEnvironment()).append("\n");
        prompt.append("- Passion: ").append(moodEntry.getPassion() != null ? moodEntry.getPassion() : "Not specified")
                .append("\n");
        prompt.append("- Description: ")
                .append(moodEntry.getDescription() != null ? moodEntry.getDescription() : "None").append("\n");
        prompt.append("- Location: ")
                .append(moodEntry.getLocation() != null ? moodEntry.getLocation() : "Not specified").append("\n\n");

        prompt.append("Please provide 3 activities in the following JSON format:\n");
        prompt.append("[\n");
        prompt.append("  {\n");
        prompt.append("    \"title\": \"Activity title\",\n");
        prompt.append("    \"description\": \"Detailed description of the activity\",\n");
        prompt.append(
                "    \"type\": \"breathing|mindfulness|physical|social|creative|self_care|learning|nature|gratitude\",\n");
        prompt.append("    \"duration\": \"Estimated duration in minutes\",\n");
        prompt.append("    \"difficulty\": \"1-5 scale where 1 is very easy and 5 is challenging\",\n");
        prompt.append("    \"priority\": \"1-5 scale where 1 is low priority and 5 is high priority\"\n");
        prompt.append("  }\n");
        prompt.append("]\n\n");

        prompt.append("Focus on activities that are:\n");
        prompt.append("- Scientifically proven to improve mental health\n");
        prompt.append("- Accessible and realistic for the user's current situation\n");
        prompt.append("- Personalized to their specific emotional state and interests\n");
        prompt.append("- Safe and appropriate for their energy level\n");

        return prompt.toString();
    }

    @Override
    public List<SuggestedActivity> processGeminiResponse(String geminiResponse, MoodEntry moodEntry) {
        try {
            // Parse Gemini response and convert to SuggestedActivity objects
            // This is a simplified implementation - in production you'd want proper JSON
            // parsing
            log.info("Processing Gemini response: {}", geminiResponse);

            // For now, return rule-based suggestions as fallback
            // In production, implement proper JSON parsing of Gemini response
            return generateRuleBasedSuggestions(moodEntry);

        } catch (Exception e) {
            log.error("Error processing Gemini response, using rule-based suggestions", e);
            return generateRuleBasedSuggestions(moodEntry);
        }
    }

    private String callGeminiAPI(String prompt) {
        // Implementation for calling Gemini API
        // This would use RestTemplate to call the Gemini API
        log.info("Calling Gemini API with prompt length: {}", prompt.length());

        // Placeholder implementation - in production, implement actual API call
        return "Gemini API response placeholder";
    }

    private List<SuggestedActivity> generateRuleBasedSuggestions(MoodEntry moodEntry) {
        List<SuggestedActivity> suggestions = new ArrayList<>();

        // Strategy 1: Energy-based activity
        suggestions.add(generateEnergyBasedActivity(moodEntry));

        // Strategy 2: Emotion-based activity
        suggestions.add(generateEmotionBasedActivity(moodEntry));

        // Strategy 3: Passion-based activity
        suggestions.add(generatePassionBasedActivity(moodEntry));

        return suggestions;
    }

    private SuggestedActivity generateEnergyBasedActivity(MoodEntry moodEntry) {
        if (moodEntry.getEnergyLevel() <= 2) {
            return createSuggestion(moodEntry,
                    "Take 5 Deep Mindful Breaths",
                    "Find a comfortable position and take 5 slow, deep breaths. Focus on the sensation of breathing in calm and breathing out tension.",
                    "breathing",
                    2,
                    1);
        } else if (moodEntry.getEnergyLevel() == 3) {
            return createSuggestion(moodEntry,
                    "Listen to Your Favorite Song",
                    "Put on a song that makes you feel good and allow yourself to truly listen. Maybe dance a little if you feel like it.",
                    "self_care",
                    4,
                    2);
        } else {
            return createSuggestion(moodEntry,
                    "Take a 5-Minute Walk Outside",
                    "Step outside and walk around your neighborhood or nearest outdoor space. Notice three things you see, hear, or smell.",
                    "physical",
                    7,
                    2);
        }
    }

    private SuggestedActivity generateEmotionBasedActivity(MoodEntry moodEntry) {
        String primaryEmotion = determinePrimaryEmotionCategory(moodEntry.getEmotions());

        switch (primaryEmotion) {
            case "sadness":
                return createSuggestion(moodEntry,
                        "Write Down Three Things You're Grateful For",
                        "Take a moment to think about and write down three things, big or small, that you appreciate in your life right now.",
                        "gratitude",
                        5,
                        2);
            case "anger":
                return createSuggestion(moodEntry,
                        "Try the 4-7-8 Breathing Technique",
                        "Breathe in for 4 counts, hold for 7 counts, breathe out for 8 counts. Repeat 3 times to help calm your nervous system.",
                        "breathing",
                        3,
                        2);
            case "fear":
            case "anxiety":
                return createSuggestion(moodEntry,
                        "Ground Yourself with 5-4-3-2-1",
                        "Name 5 things you can see, 4 things you can touch, 3 things you can hear, 2 things you can smell, and 1 thing you can taste.",
                        "mindfulness",
                        4,
                        2);
            case "joy":
                return createSuggestion(moodEntry,
                        "Share Your Joy with Someone",
                        "Send a message to someone you care about sharing something positive from your day or simply asking how they're doing.",
                        "social",
                        3,
                        2);
            default:
                return createSuggestion(moodEntry,
                        "Try a 2-Minute Mindfulness Moment",
                        "Sit quietly and focus on your breath for 2 minutes. Notice when your mind wanders and gently bring attention back to breathing.",
                        "mindfulness",
                        2,
                        1);
        }
    }

    private SuggestedActivity generatePassionBasedActivity(MoodEntry moodEntry) {
        if (moodEntry.getPassion() != null && !moodEntry.getPassion().trim().isEmpty()) {
            String passion = moodEntry.getPassion().toLowerCase();

            if (passion.contains("music") || passion.contains("art") || passion.contains("creative")) {
                return createSuggestion(moodEntry,
                        "Express Your Creativity",
                        "Take 10 minutes to engage with your passion. Play an instrument, draw, write, or create something that brings you joy.",
                        "creative",
                        10,
                        3);
            } else if (passion.contains("sport") || passion.contains("fitness") || passion.contains("exercise")) {
                return createSuggestion(moodEntry,
                        "Light Physical Activity",
                        "Do a gentle workout related to your passion. Even 5-10 minutes of movement can boost your mood and energy.",
                        "physical",
                        10,
                        3);
            } else if (passion.contains("reading") || passion.contains("learning") || passion.contains("study")) {
                return createSuggestion(moodEntry,
                        "Learn Something New",
                        "Spend 15 minutes exploring a topic related to your passion. Read an article, watch a short video, or research something interesting.",
                        "learning",
                        15,
                        2);
            }
        }

        // Default passion-based activity
        return createSuggestion(moodEntry,
                "Reflect on Your Passion",
                "Take a moment to think about what truly excites you and how you can incorporate it into your day, even in a small way.",
                "self_care",
                5,
                1);
    }

    private SuggestedActivity createSuggestion(MoodEntry moodEntry, String title, String description,
            String activityType, Integer duration, Integer difficulty) {
        return SuggestedActivity.builder()
                .moodEntry(moodEntry)
                .activityDescription(description)
                .activityType(activityType)
                .estimatedDurationMinutes(duration)
                .difficultyLevel(difficulty)
                .priorityLevel(3) // Medium priority by default
                .isCompleted(false)
                .build();
    }

    private String determinePrimaryEmotionCategory(Set<Emotion> emotions) {
        if (emotions == null || emotions.isEmpty()) {
            return "neutral";
        }

        Map<String, Integer> categoryCount = new HashMap<>();
        for (Emotion emotion : emotions) {
            String category = emotion.getParent() != null ? emotion.getParent().getKey() : emotion.getKey();
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
        }

        return categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("neutral");
    }

    private String getEmotionLabels(Set<Emotion> emotions) {
        if (emotions == null || emotions.isEmpty()) {
            return "None";
        }
        return emotions.stream()
                .map(Emotion::getLabel)
                .collect(Collectors.joining(", "));
    }

    private List<SuggestedActivity> generateFallbackSuggestions(MoodEntry moodEntry) {
        log.warn("Using fallback suggestions for mood entry: {}", moodEntry.getId());

        List<SuggestedActivity> fallbacks = new ArrayList<>();

        fallbacks.add(createSuggestion(moodEntry,
                "Take Three Deep Breaths",
                "Find a quiet moment and take three slow, deep breaths. Focus on the rhythm of your breathing.",
                "breathing",
                1,
                1));

        fallbacks.add(createSuggestion(moodEntry,
                "Notice Something Beautiful",
                "Look around you and find one thing that catches your eye - a color, texture, or pattern that you find pleasing.",
                "mindfulness",
                2,
                1));

        fallbacks.add(createSuggestion(moodEntry,
                "Send a Kind Message",
                "Think of someone who might appreciate a friendly message and send them a brief note of care or appreciation.",
                "social",
                3,
                2));

        return fallbacks;
    }
}