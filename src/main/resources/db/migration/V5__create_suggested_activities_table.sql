-- Create suggested_activities table for AI-generated activity recommendations
CREATE TABLE IF NOT EXISTS suggested_activities (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    mood_entry_id BIGINT NOT NULL,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    estimated_duration_minutes INTEGER,
    difficulty_level VARCHAR(50),
    is_completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP,
    suggested_date DATE DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_suggested_activities_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_suggested_activities_mood_entry FOREIGN KEY (mood_entry_id) REFERENCES mood_entries(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_suggested_activities_user_id ON suggested_activities(user_id);
CREATE INDEX IF NOT EXISTS idx_suggested_activities_mood_entry_id ON suggested_activities(mood_entry_id);
CREATE INDEX IF NOT EXISTS idx_suggested_activities_suggested_date ON suggested_activities(suggested_date);
CREATE INDEX IF NOT EXISTS idx_suggested_activities_is_completed ON suggested_activities(is_completed);
CREATE INDEX IF NOT EXISTS idx_suggested_activities_category ON suggested_activities(category);

-- Add trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_suggested_activities_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_suggested_activities_updated_at
    BEFORE UPDATE ON suggested_activities
    FOR EACH ROW
    EXECUTE FUNCTION update_suggested_activities_updated_at_column();
