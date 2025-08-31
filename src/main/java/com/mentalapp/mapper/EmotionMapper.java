package com.mentalapp.mapper;

import com.mentalapp.model.Emotion;
import com.mentalapp.dto.MoodEntryResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmotionMapper {

    @Mapping(target = "parentKey", source = "parent.key")
    MoodEntryResponse.EmotionResponse toResponse(Emotion emotion);

    @IterableMapping(elementTargetType = MoodEntryResponse.EmotionResponse.class)
    List<MoodEntryResponse.EmotionResponse> toResponseList(List<Emotion> emotions);
}