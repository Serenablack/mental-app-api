-- Create mood_entries table for tracking user mood logs
CREATE TABLE IF NOT EXISTS mood_entries (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    location VARCHAR(500),
    environment VARCHAR(50) NOT NULL,
    description TEXT,
    energy_level INTEGER NOT NULL CHECK (energy_level >= 1 AND energy_level <= 5),
    entry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_voice_input BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_mood_entries_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create mood_entry_emotions table for storing selected emotions (many-to-many relationship)
CREATE TABLE IF NOT EXISTS mood_entry_emotions (
    mood_entry_id BIGINT NOT NULL,
    emotion_key VARCHAR(100) NOT NULL,
    PRIMARY KEY (mood_entry_id, emotion_key),
    CONSTRAINT fk_mood_entry_emotions_mood_entry FOREIGN KEY (mood_entry_id) REFERENCES mood_entries(id) ON DELETE CASCADE,
    CONSTRAINT fk_mood_entry_emotions_emotion FOREIGN KEY (emotion_key) REFERENCES emotions(key) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_mood_entries_user_id ON mood_entries(user_id);
CREATE INDEX IF NOT EXISTS idx_mood_entries_entry_date ON mood_entries(entry_date);
CREATE INDEX IF NOT EXISTS idx_mood_entries_energy_level ON mood_entries(energy_level);
CREATE INDEX IF NOT EXISTS idx_mood_entry_emotions_emotion_key ON mood_entry_emotions(emotion_key);

-- Add trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_mood_entries_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_mood_entries_updated_at
    BEFORE UPDATE ON mood_entries
    FOR EACH ROW
    EXECUTE FUNCTION update_mood_entries_updated_at_column();
