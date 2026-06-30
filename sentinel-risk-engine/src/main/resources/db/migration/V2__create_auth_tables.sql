-- ============================================================
-- Users Table
-- ============================================================

CREATE TABLE users
(
    id UUID PRIMARY KEY,

    first_name VARCHAR(150) NOT NULL,

    last_name VARCHAR(150) NOT NULL,

    email VARCHAR(255) NOT NULL,

    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(30) NOT NULL,

    status VARCHAR(30) NOT NULL,

    email_verified BOOLEAN NOT NULL DEFAULT FALSE,

    failed_login_attempts INTEGER NOT NULL DEFAULT 0,

    last_login_at TIMESTAMP WITH TIME ZONE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE,

    version BIGINT
);

ALTER TABLE users
    ADD CONSTRAINT uk_users_email UNIQUE (email);

CREATE INDEX idx_users_email
    ON users(email);

CREATE INDEX idx_users_status
    ON users(status);

-- ============================================================
-- Refresh Tokens
-- ============================================================

CREATE TABLE refresh_tokens
(
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    token VARCHAR(512) NOT NULL,

    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,

    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE,

    version BIGINT,

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
);

CREATE INDEX idx_refresh_token_user
    ON refresh_tokens(user_id);

CREATE INDEX idx_refresh_token_token
    ON refresh_tokens(token);