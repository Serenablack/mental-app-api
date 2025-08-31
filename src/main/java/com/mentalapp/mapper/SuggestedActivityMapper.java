package com.mentalapp.mapper;

import com.mentalapp.model.SuggestedActivity;
import com.mentalapp.dto.SuggestedActivityResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SuggestedActivityMapper {

    @Mapping(target = "moodEntryId", source = "moodEntry.id")
    SuggestedActivityResponse toResponse(SuggestedActivity activity);

    @IterableMapping(elementTargetType = SuggestedActivityResponse.class)
    List<SuggestedActivityResponse> toResponseList(List<SuggestedActivity> activities);
}