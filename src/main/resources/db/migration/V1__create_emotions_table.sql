-- Create emotions table for Emotion Wheel taxonomy
CREATE TABLE IF NOT EXISTS emotions (
    id BIGSERIAL PRIMARY KEY,
    key VARCHAR(100) UNIQUE NOT NULL,
    label VARCHAR(255) NOT NULL,
    parent_key VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_emotions_parent FOREIGN KEY (parent_key) REFERENCES emotions(key) ON DELETE CASCADE
);

-- Create index for better query performance
CREATE INDEX IF NOT EXISTS idx_emotions_key ON emotions(key);
CREATE INDEX IF NOT EXISTS idx_emotions_parent_key ON emotions(parent_key);

-- Add trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_emotions_updated_at
    BEFORE UPDATE ON emotions
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();



