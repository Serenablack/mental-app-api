package com.mentalapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mood_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mood_entry_emotions", joinColumns = @JoinColumn(name = "mood_entry_id"))
    @Column(name = "emotion_key")
    private List<String> emotionKeys;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "environment", nullable = false)
    private Environment environment;

    @Column(name = "description", length = 5000)
    private String description;

    @Column(name = "energy_level", nullable = false)
    private Integer energyLevel; // 1-5 scale

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_voice_input")
    private Boolean isVoiceInput = false;

    @OneToMany(mappedBy = "moodEntry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SuggestedActivity> suggestedActivities;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (entryDate == null) {
            entryDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Environment {
        ALONE("Alone"),
        IN_GROUP("In a group");

        private final String displayName;

        Environment(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
