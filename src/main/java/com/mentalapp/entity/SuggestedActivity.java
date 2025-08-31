package com.mentalapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "suggested_activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestedActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mood_entry_id", nullable = false)
    @JsonIgnore
    private MoodEntry moodEntry;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ActivityCategory category;

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level")
    private DifficultyLevel difficultyLevel;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "suggested_date", nullable = false)
    private LocalDate suggestedDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (suggestedDate == null) {
            suggestedDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (isCompleted && completedAt == null) {
            completedAt = LocalDateTime.now();
        }
    }

    public enum ActivityCategory {
        BREATHING("Breathing & Relaxation"),
        PHYSICAL("Physical Activity"),
        SOCIAL("Social Connection"),
        CREATIVE("Creative Expression"),
        MINDFULNESS("Mindfulness & Meditation"),
        SELF_CARE("Self Care"),
        PRODUCTIVITY("Productive Tasks"),
        NATURE("Nature & Outdoors"),
        LEARNING("Learning & Growth"),
        GRATITUDE("Gratitude & Reflection");

        private final String displayName;

        ActivityCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DifficultyLevel {
        VERY_EASY("Very Easy (1-2 min)"),
        EASY("Easy (3-5 min)"),
        MODERATE("Moderate (6-15 min)"),
        CHALLENGING("Challenging (16-30 min)"),
        INTENSIVE("Intensive (30+ min)");

        private final String displayName;

        DifficultyLevel(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
