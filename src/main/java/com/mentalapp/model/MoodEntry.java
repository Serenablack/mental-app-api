package com.mentalapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mood_entries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "comfort_environment", length = 50)
    private String comfortEnvironment;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "energy_level")
    private Integer energyLevel;

    @Column(name = "passion", length = 100)
    private String passion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "mood_entry_emotions",
        joinColumns = @JoinColumn(name = "mood_entry_id"),
        inverseJoinColumns = @JoinColumn(name = "emotion_id")
    )
    private Set<Emotion> emotions = new HashSet<>();

    @OneToMany(mappedBy = "moodEntry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SuggestedActivity> suggestedActivities = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void addEmotion(Emotion emotion) {
        this.emotions.add(emotion);
    }

    public void removeEmotion(Emotion emotion) {
        this.emotions.remove(emotion);
    }

    public void addSuggestedActivity(SuggestedActivity activity) {
        this.suggestedActivities.add(activity);
        activity.setMoodEntry(this);
    }

    public void removeSuggestedActivity(SuggestedActivity activity) {
        this.suggestedActivities.remove(activity);
        activity.setMoodEntry(null);
    }

    public boolean isFromToday() {
        LocalDateTime now = LocalDateTime.now();
        return this.entryDate.toLocalDate().equals(now.toLocalDate());
    }
}


