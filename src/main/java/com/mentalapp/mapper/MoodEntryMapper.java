package com.mentalapp.mapper;

import com.mentalapp.model.Emotion;
import com.mentalapp.model.MoodEntry;
import com.mentalapp.model.SuggestedActivity;
import com.mentalapp.dto.MoodEntryCreateRequest;
import com.mentalapp.dto.MoodEntryResponse;
import com.mentalapp.dto.MoodEntryUpdateRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MoodEntryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "emotions", ignore = true)
    @Mapping(target = "suggestedActivities", ignore = true)
    MoodEntry toEntity(MoodEntryCreateRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "isFromToday", expression = "java(moodEntry.isFromToday())")
    MoodEntryResponse toResponse(MoodEntry moodEntry);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "emotions", ignore = true)
    @Mapping(target = "suggestedActivities", ignore = true)
    void updateEntity(@MappingTarget MoodEntry entity, MoodEntryUpdateRequest request);

    @Named("mapEmotionToResponse")
    default MoodEntryResponse.EmotionResponse mapEmotion(Emotion emotion) {
        if (emotion == null) {
            return null;
        }
        return MoodEntryResponse.EmotionResponse.builder()
                .id(emotion.getId())
                .key(emotion.getKey())
                .label(emotion.getLabel())
                .parentKey(emotion.getParent() != null ? emotion.getParent().getKey() : null)
                .build();
    }

    @Named("mapActivityToResponse")
    default MoodEntryResponse.SuggestedActivityResponse mapActivity(SuggestedActivity activity) {
        if (activity == null) {
            return null;
        }
        return MoodEntryResponse.SuggestedActivityResponse.builder()
                .id(activity.getId())
                .activityDescription(activity.getActivityDescription())
                .isCompleted(activity.getIsCompleted())
                .completedAt(activity.getCompletedAt())
                .activityType(activity.getActivityType())
                .estimatedDurationMinutes(activity.getEstimatedDurationMinutes())
                .difficultyLevel(activity.getDifficultyLevel())
                .priorityLevel(activity.getPriorityLevel())
                .status(activity.getStatus())
                .createdAt(activity.getCreatedAt())
                .build();
    }
}