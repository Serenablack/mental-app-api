-- Create mood_entry_emotions table for many-to-many relationship
CREATE TABLE IF NOT EXISTS mood_entry_emotions (
    mood_entry_id BIGINT NOT NULL,
    emotion_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (mood_entry_id, emotion_id),
    FOREIGN KEY (mood_entry_id) REFERENCES mood_entries(id) ON DELETE CASCADE,
    FOREIGN KEY (emotion_id) REFERENCES emotions(id) ON DELETE CASCADE
);

-- Create index for better performance
CREATE INDEX IF NOT EXISTS idx_mood_entry_emotions_mood_entry_id ON mood_entry_emotions(mood_entry_id);
CREATE INDEX IF NOT EXISTS idx_mood_entry_emotions_emotion_id ON mood_entry_emotions(emotion_id);


