package com.mentalapp.service;

import com.mentalapp.entity.MoodEntry;
import com.mentalapp.entity.SuggestedActivity;
import com.mentalapp.entity.Emotion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIActivitySuggestionService {

    private final EmotionService emotionService;

    /**
     * Generate AI-powered activity suggestions based on mood entry
     */
    public List<SuggestedActivity> generateActivitySuggestions(MoodEntry moodEntry) {
        log.info("Generating AI activity suggestions for mood entry: {}", moodEntry.getId());

        List<SuggestedActivity> suggestions = new ArrayList<>();

        // Analyze mood patterns and energy level
        AnalysisResult analysis = analyzeMoodEntry(moodEntry);

        // Generate 3 targeted suggestions based on analysis
        suggestions.addAll(generateTargetedSuggestions(moodEntry, analysis));

        return suggestions;
    }

    private AnalysisResult analyzeMoodEntry(MoodEntry moodEntry) {
        AnalysisResult analysis = new AnalysisResult();

        // Analyze emotions
        analysis.primaryEmotionCategory = determinePrimaryEmotionCategory(moodEntry.getEmotionKeys());
        analysis.emotionIntensity = determineEmotionIntensity(moodEntry.getEmotionKeys());
        analysis.energyLevel = moodEntry.getEnergyLevel();
        analysis.isAlone = moodEntry.getEnvironment() == MoodEntry.Environment.ALONE;
        analysis.hasLocation = moodEntry.getLocation() != null && !moodEntry.getLocation().trim().isEmpty();
        analysis.hasDescription = moodEntry.getDescription() != null && !moodEntry.getDescription().trim().isEmpty();

        return analysis;
    }

    private List<SuggestedActivity> generateTargetedSuggestions(MoodEntry moodEntry, AnalysisResult analysis) {
        List<SuggestedActivity> suggestions = new ArrayList<>();

        // Strategy 1: Energy-based activity
        suggestions.add(generateEnergyBasedActivity(moodEntry, analysis));

        // Strategy 2: Emotion-based activity
        suggestions.add(generateEmotionBasedActivity(moodEntry, analysis));

        // Strategy 3: Environment and context-based activity
        suggestions.add(generateContextBasedActivity(moodEntry, analysis));

        return suggestions;
    }

    private SuggestedActivity generateEnergyBasedActivity(MoodEntry moodEntry, AnalysisResult analysis) {
        if (analysis.energyLevel <= 2) {
            // Low energy - gentle, restorative activities
            return createSuggestion(moodEntry,
                    "Take 5 Deep Mindful Breaths",
                    "Find a comfortable position and take 5 slow, deep breaths. Focus on the sensation of breathing in calm and breathing out tension.",
                    SuggestedActivity.ActivityCategory.BREATHING,
                    2,
                    SuggestedActivity.DifficultyLevel.VERY_EASY);
        } else if (analysis.energyLevel == 3) {
            // Medium energy - balanced activities
            return createSuggestion(moodEntry,
                    "Listen to Your Favorite Song",
                    "Put on a song that makes you feel good and allow yourself to truly listen. Maybe dance a little if you feel like it.",
                    SuggestedActivity.ActivityCategory.SELF_CARE,
                    4,
                    SuggestedActivity.DifficultyLevel.EASY);
        } else {
            // High energy - more active suggestions
            return createSuggestion(moodEntry,
                    "Take a 5-Minute Walk Outside",
                    "Step outside and walk around your neighborhood or nearest outdoor space. Notice three things you see, hear, or smell.",
                    SuggestedActivity.ActivityCategory.PHYSICAL,
                    7,
                    SuggestedActivity.DifficultyLevel.EASY);
        }
    }

    private SuggestedActivity generateEmotionBasedActivity(MoodEntry moodEntry, AnalysisResult analysis) {
        switch (analysis.primaryEmotionCategory) {
            case "sadness":
                return createSuggestion(moodEntry,
                        "Write Down Three Things You're Grateful For",
                        "Take a moment to think about and write down three things, big or small, that you appreciate in your life right now.",
                        SuggestedActivity.ActivityCategory.GRATITUDE,
                        5,
                        SuggestedActivity.DifficultyLevel.EASY);
            case "anger":
                return createSuggestion(moodEntry,
                        "Try the 4-7-8 Breathing Technique",
                        "Breathe in for 4 counts, hold for 7 counts, breathe out for 8 counts. Repeat 3 times to help calm your nervous system.",
                        SuggestedActivity.ActivityCategory.BREATHING,
                        3,
                        SuggestedActivity.DifficultyLevel.EASY);
            case "fear":
            case "anxiety":
                return createSuggestion(moodEntry,
                        "Ground Yourself with 5-4-3-2-1",
                        "Name 5 things you can see, 4 things you can touch, 3 things you can hear, 2 things you can smell, and 1 thing you can taste.",
                        SuggestedActivity.ActivityCategory.MINDFULNESS,
                        4,
                        SuggestedActivity.DifficultyLevel.EASY);
            case "joy":
                return createSuggestion(moodEntry,
                        "Share Your Joy with Someone",
                        "Send a message to someone you care about sharing something positive from your day or simply asking how they're doing.",
                        SuggestedActivity.ActivityCategory.SOCIAL,
                        3,
                        SuggestedActivity.DifficultyLevel.EASY);
            case "neutral":
                return createSuggestion(moodEntry,
                        "Try a 2-Minute Mindfulness Moment",
                        "Sit quietly and focus on your breath for 2 minutes. Notice when your mind wanders and gently bring attention back to breathing.",
                        SuggestedActivity.ActivityCategory.MINDFULNESS,
                        2,
                        SuggestedActivity.DifficultyLevel.VERY_EASY);
            default:
                return createSuggestion(moodEntry,
                        "Do One Small Act of Self-Care",
                        "Choose something small and nurturing for yourself: make your favorite drink, wash your face, or tidy up one small space.",
                        SuggestedActivity.ActivityCategory.SELF_CARE,
                        5,
                        SuggestedActivity.DifficultyLevel.EASY);
        }
    }

    private SuggestedActivity generateContextBasedActivity(MoodEntry moodEntry, AnalysisResult analysis) {
        if (analysis.isAlone) {
            if (analysis.hasDescription && moodEntry.getDescription().toLowerCase().contains("stress")) {
                return createSuggestion(moodEntry,
                        "Progressive Muscle Relaxation",
                        "Tense and then relax each muscle group in your body, starting from your toes and working up to your head.",
                        SuggestedActivity.ActivityCategory.BREATHING,
                        10,
                        SuggestedActivity.DifficultyLevel.MODERATE);
            } else {
                return createSuggestion(moodEntry,
                        "Create Something Small",
                        "Draw a doodle, write a few sentences about your day, or arrange something beautiful in your space. Express yourself creatively.",
                        SuggestedActivity.ActivityCategory.CREATIVE,
                        8,
                        SuggestedActivity.DifficultyLevel.EASY);
            }
        } else {
            return createSuggestion(moodEntry,
                    "Practice Active Listening",
                    "In your next conversation, focus completely on what the other person is saying without thinking about your response.",
                    SuggestedActivity.ActivityCategory.SOCIAL,
                    0, // Ongoing activity
                    SuggestedActivity.DifficultyLevel.MODERATE);
        }
    }

    private SuggestedActivity createSuggestion(MoodEntry moodEntry, String title, String description,
            SuggestedActivity.ActivityCategory category, Integer duration,
            SuggestedActivity.DifficultyLevel difficulty) {
        return SuggestedActivity.builder()
                .user(moodEntry.getUser())
                .moodEntry(moodEntry)
                .title(title)
                .description(description)
                .category(category)
                .estimatedDurationMinutes(duration)
                .difficultyLevel(difficulty)
                .isCompleted(false)
                .build();
    }

    private String determinePrimaryEmotionCategory(List<String> emotionKeys) {
        if (emotionKeys == null || emotionKeys.isEmpty()) {
            return "neutral";
        }

        // Get emotions and find their parent categories
        Map<String, Integer> categoryCount = new HashMap<>();

        for (String emotionKey : emotionKeys) {
            Optional<Emotion> emotion = emotionService.getEmotionByKey(emotionKey);
            if (emotion.isPresent()) {
                String category = emotion.get().getParent() != null ? emotion.get().getParent().getKey() : emotionKey;
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            }
        }

        return categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("neutral");
    }

    private String determineEmotionIntensity(List<String> emotionKeys) {
        if (emotionKeys == null || emotionKeys.size() <= 1) {
            return "low";
        } else if (emotionKeys.size() <= 3) {
            return "medium";
        } else {
            return "high";
        }
    }

    private static class AnalysisResult {
        String primaryEmotionCategory;
        String emotionIntensity;
        Integer energyLevel;
        boolean isAlone;
        boolean hasLocation;
        boolean hasDescription;
    }
}
