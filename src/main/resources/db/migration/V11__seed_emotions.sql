-- Seed Emotion Wheel taxonomy based on scientific research
-- Using INSERT ... ON CONFLICT DO NOTHING for idempotent safety
-- Structured for dropdown lists with main emotions and sub-emotions

-- First, create the main emotion categories (root level)
INSERT INTO emotions (key, label, parent_key) VALUES 
('joy', 'Joy', NULL),
('trust', 'Trust', NULL),
('anticipation', 'Anticipation', NULL),
('serenity', 'Serenity', NULL),
('acceptance', 'Acceptance', NULL),
('anger', 'Anger', NULL),
('fear', 'Fear', NULL),
('surprise', 'Surprise', NULL),
('sadness', 'Sadness', NULL),
('disgust', 'Disgust', NULL),
('remorse', 'Remorse', NULL),
('neutral', 'Neutral', NULL)
ON CONFLICT (key) DO NOTHING;

-- Joy and its sub-emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('contentment', 'Contentment', 'joy'),
('pride', 'Pride', 'joy'),
('amusement', 'Amusement', 'joy'),
('optimism', 'Optimism', 'joy'),
('excitement', 'Excitement', 'joy'),
('enthusiasm', 'Enthusiasm', 'joy'),
('elation', 'Elation', 'joy'),
('happiness', 'Happiness', 'joy'),
('pleasure', 'Pleasure', 'joy'),
('delight', 'Delight', 'joy')
ON CONFLICT (key) DO NOTHING;

-- Trust/Love and its sub-emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('affection', 'Affection', 'trust'),
('compassion', 'Compassion', 'trust'),
('caring', 'Caring', 'trust'),
('love', 'Love', 'trust'),
('empathy', 'Empathy', 'trust'),
('gratitude', 'Gratitude', 'trust'),
('appreciation', 'Appreciation', 'trust'),
('admiration', 'Admiration', 'trust'),
('devotion', 'Devotion', 'trust'),
('loyalty', 'Loyalty', 'trust')
ON CONFLICT (key) DO NOTHING;

-- Anticipation/Curiosity and its sub-emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('interest', 'Interest', 'anticipation'),
('vigilance', 'Vigilance', 'anticipation'),
('attention', 'Attention', 'anticipation'),
('curiosity', 'Curiosity', 'anticipation'),
('eagerness', 'Eagerness', 'anticipation'),
('hope', 'Hope', 'anticipation'),
('expectancy', 'Expectancy', 'anticipation'),
('alertness', 'Alertness', 'anticipation'),
('readiness', 'Readiness', 'anticipation'),
('anticipatory_joy', 'Anticipatory Joy', 'anticipation')
ON CONFLICT (key) DO NOTHING;

-- Serenity and its sub-emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('calmness', 'Calmness', 'serenity'),
('peacefulness', 'Peacefulness', 'serenity'),
('tranquility', 'Tranquility', 'serenity'),
('relaxation', 'Relaxation', 'serenity'),
('satisfaction', 'Satisfaction', 'serenity'),
('comfort', 'Comfort', 'serenity'),
('ease', 'Ease', 'serenity'),
('sereneness', 'Sereneness', 'serenity'),
('quietude', 'Quietude', 'serenity'),
('repose', 'Repose', 'serenity')
ON CONFLICT (key) DO NOTHING;

-- Acceptance and its sub-emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('openness', 'Openness', 'acceptance'),
('receptiveness', 'Receptiveness', 'acceptance'),
('tolerance', 'Tolerance', 'acceptance'),
('understanding', 'Understanding', 'acceptance'),
('approval', 'Approval', 'acceptance'),
('welcome', 'Welcome', 'acceptance'),
('embrace', 'Embrace', 'acceptance'),
('inclusiveness', 'Inclusiveness', 'acceptance'),
('acknowledgment', 'Acknowledgment', 'acceptance'),
('recognition', 'Recognition', 'acceptance')
ON CONFLICT (key) DO NOTHING;

-- Anger and its sub-emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('frustration', 'Frustration', 'anger'),
('resentment', 'Resentment', 'anger'),
('envy', 'Envy', 'anger'),
('rage', 'Rage', 'anger'),
('irritation', 'Irritation', 'anger'),
('annoyance', 'Annoyance', 'anger'),
('hostility', 'Hostility', 'anger'),
('jealousy', 'Jealousy', 'anger'),
('contempt', 'Contempt', 'anger'),
('outrage', 'Outrage', 'anger'),
('fury', 'Fury', 'anger'),
('wrath', 'Wrath', 'anger')
ON CONFLICT (key) DO NOTHING;

-- Fear and its sub-emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('anxiety', 'Anxiety', 'fear'),
('worry', 'Worry', 'fear'),
('nervousness', 'Nervousness', 'fear'),
('panic', 'Panic', 'fear'),
('terror', 'Terror', 'fear'),
('apprehension', 'Apprehension', 'fear'),
('dread', 'Dread', 'fear'),
('unease', 'Unease', 'fear'),
('stress', 'Stress', 'fear'),
('fright', 'Fright', 'fear'),
('alarm', 'Alarm', 'fear'),
('distress', 'Distress', 'fear')
ON CONFLICT (key) DO NOTHING;

-- Surprise and its sub-emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('shock', 'Shock', 'surprise'),
('awe', 'Awe', 'surprise'),
('confusion', 'Confusion', 'surprise'),
('bewilderment', 'Bewilderment', 'surprise'),
('amazement', 'Amazement', 'surprise'),
('astonishment', 'Astonishment', 'surprise'),
('disbelief', 'Disbelief', 'surprise'),
('perplexity', 'Perplexity', 'surprise'),
('wonder', 'Wonder', 'surprise'),
('startlement', 'Startlement', 'surprise'),
('stupefaction', 'Stupefaction', 'surprise'),
('incredulity', 'Incredulity', 'surprise')
ON CONFLICT (key) DO NOTHING;

-- Sadness and its sub-emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('grief', 'Grief', 'sadness'),
('loneliness', 'Loneliness', 'sadness'),
('disappointment', 'Disappointment', 'sadness'),
('tiredness', 'Tiredness', 'sadness'),
('melancholy', 'Melancholy', 'sadness'),
('sorrow', 'Sorrow', 'sadness'),
('despair', 'Despair', 'sadness'),
('hopelessness', 'Hopelessness', 'sadness'),
('depression', 'Depression', 'sadness'),
('emptiness', 'Emptiness', 'sadness'),
('isolation', 'Isolation', 'sadness'),
('dejection', 'Dejection', 'sadness'),
('gloom', 'Gloom', 'sadness'),
('despondency', 'Despondency', 'sadness')
ON CONFLICT (key) DO NOTHING;

-- Disgust and its sub-emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('aversion', 'Aversion', 'disgust'),
('judgment', 'Judgment', 'disgust'),
('distaste', 'Distaste', 'disgust'),
('repulsion', 'Repulsion', 'disgust'),
('revulsion', 'Revulsion', 'disgust'),
('loathing', 'Loathing', 'disgust'),
('abhorrence', 'Abhorrence', 'disgust'),
('contempt', 'Contempt', 'disgust'),
('disdain', 'Disdain', 'disgust'),
('scorn', 'Scorn', 'disgust'),
('repugnance', 'Repugnance', 'disgust'),
('nausea', 'Nausea', 'disgust')
ON CONFLICT (key) DO NOTHING;

-- Remorse and its sub-emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('guilt', 'Guilt', 'remorse'),
('shame', 'Shame', 'remorse'),
('regret', 'Regret', 'remorse'),
('self_blame', 'Self-Blame', 'remorse'),
('embarrassment', 'Embarrassment', 'remorse'),
('humiliation', 'Humiliation', 'remorse'),
('remorsefulness', 'Remorsefulness', 'remorse'),
('contrition', 'Contrition', 'remorse'),
('penitence', 'Penitence', 'remorse'),
('compunction', 'Compunction', 'remorse'),
('chagrin', 'Chagrin', 'remorse'),
('mortification', 'Mortification', 'remorse')
ON CONFLICT (key) DO NOTHING;

-- Neutral/calm states
INSERT INTO emotions (key, label, parent_key) VALUES 
('indifference', 'Indifference', 'neutral'),
('detachment', 'Detachment', 'neutral'),
('apathy', 'Apathy', 'neutral'),
('boredom', 'Boredom', 'neutral'),
('numbness', 'Numbness', 'neutral'),
('equanimity', 'Equanimity', 'neutral'),
('impartiality', 'Impartiality', 'neutral'),
('objectivity', 'Objectivity', 'neutral'),
('disinterest', 'Disinterest', 'neutral'),
('unconcern', 'Unconcern', 'neutral')
ON CONFLICT (key) DO NOTHING;

-- Additional nuanced emotions for comprehensive coverage

-- Mixed emotions (can be categorized under multiple parents)
INSERT INTO emotions (key, label, parent_key) VALUES 
('nostalgia', 'Nostalgia', 'serenity'),
('longing', 'Longing', 'sadness'),
('yearning', 'Yearning', 'sadness'),
('bittersweet', 'Bittersweet', 'serenity'),
('melancholic_joy', 'Melancholic Joy', 'joy'),
('peaceful_sadness', 'Peaceful Sadness', 'sadness'),
('anxious_excitement', 'Anxious Excitement', 'anticipation'),
('angry_sadness', 'Angry Sadness', 'sadness'),
('fearful_curiosity', 'Fearful Curiosity', 'anticipation'),
('surprised_joy', 'Surprised Joy', 'joy')
ON CONFLICT (key) DO NOTHING;

-- Complex emotions
INSERT INTO emotions (key, label, parent_key) VALUES 
('ambivalence', 'Ambivalence', 'neutral'),
('conflicted', 'Conflicted', 'neutral'),
('uncertainty', 'Uncertainty', 'neutral'),
('indecision', 'Indecision', 'neutral'),
('mixed_feelings', 'Mixed Feelings', 'neutral'),
('confusion', 'Confusion', 'neutral'),
('doubt', 'Doubt', 'neutral'),
('hesitation', 'Hesitation', 'neutral'),
('wavering', 'Wavering', 'neutral'),
('torn', 'Torn', 'neutral')
ON CONFLICT (key) DO NOTHING;




