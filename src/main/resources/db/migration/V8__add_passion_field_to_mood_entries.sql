-- Add passion field to mood_entries table
ALTER TABLE mood_entries 
ADD COLUMN IF NOT EXISTS passion VARCHAR(100);

-- Add index for better performance on passion queries
CREATE INDEX IF NOT EXISTS idx_mood_entries_passion ON mood_entries(passion);


