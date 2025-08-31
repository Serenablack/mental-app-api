package com.mentalapp.service;

import com.mentalapp.exception.ResourceNotFoundException;
import com.mentalapp.model.SuggestedActivity;
import com.mentalapp.model.User;
import com.mentalapp.repository.SuggestedActivityRepository;
import com.mentalapp.service.interfaces.SuggestedActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuggestedActivityServiceImpl implements SuggestedActivityService {

    private final SuggestedActivityRepository suggestedActivityRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SuggestedActivity> getActivitiesByDate(User user, LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return suggestedActivityRepository.findByUserIdAndDate(user.getId(), startOfDay, endOfDay);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuggestedActivity> getActivitiesByType(User user, String activityType) {
        return suggestedActivityRepository.findByUserIdAndType(user.getId(), activityType);
    }

    @Override
    @Transactional
    public void markAsCompleted(Long id, User user) {
        SuggestedActivity activity = suggestedActivityRepository.findById(id)
                .filter(a -> a.getMoodEntry().getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));

        activity.markAsCompleted();
        suggestedActivityRepository.save(activity);
    }

    @Override
    @Transactional
    public void markAsIncomplete(Long id, User user) {
        SuggestedActivity activity = suggestedActivityRepository.findById(id)
                .filter(a -> a.getMoodEntry().getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));

        activity.markAsIncomplete();
        suggestedActivityRepository.save(activity);
    }

    @Override
    @Transactional
    public void deleteActivity(Long id, User user) {
        SuggestedActivity activity = suggestedActivityRepository.findById(id)
                .filter(a -> a.getMoodEntry().getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));

        if (activity.getIsCompleted()) {
            throw new IllegalStateException("Cannot delete completed activities");
        }

        suggestedActivityRepository.delete(activity);
    }
}