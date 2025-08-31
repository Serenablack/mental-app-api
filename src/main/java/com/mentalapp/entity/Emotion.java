package com.mentalapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "emotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emotion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "key", unique = true, nullable = false, length = 100)
    private String key;
    
    @Column(name = "label", nullable = false, length = 255)
    private String label;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_key", referencedColumnName = "key")
    private Emotion parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Emotion> children;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}




