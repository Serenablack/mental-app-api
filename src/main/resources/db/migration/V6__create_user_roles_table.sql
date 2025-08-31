-- Create user_roles table for storing user roles
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert default roles for existing users if any
INSERT INTO user_roles (user_id, role)
SELECT id, 'USER' FROM users
WHERE id NOT IN (SELECT user_id FROM user_roles)
ON CONFLICT DO NOTHING;


