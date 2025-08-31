package com.mentalapp.service.interfaces;

import com.mentalapp.model.SuggestedActivity;
import com.mentalapp.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface SuggestedActivityService {

    List<SuggestedActivity> getActivitiesByDate(User user, LocalDateTime date);

    List<SuggestedActivity> getActivitiesByType(User user, String activityType);

    void markAsCompleted(Long id, User user);

    void markAsIncomplete(Long id, User user);

    void deleteActivity(Long id, User user);
}


