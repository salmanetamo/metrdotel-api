CREATE TABLE users (
    id VARCHAR(36) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(75) NOT NULL,
    password VARCHAR(75) NOT NULL,
    profile_picture VARCHAR(50),
    enabled BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE INDEX ix_username ON users (email);

CREATE TABLE activation_tokens (
       id VARCHAR(36) NOT NULL,
       email VARCHAR(75) NOT NULL,
       token VARCHAR(75) NOT NULL,
       expires_at TIMESTAMP NOT NULL,
       PRIMARY KEY (id)
);

CREATE INDEX ix_activation_token_email ON activation_tokens (email);

CREATE TABLE password_reset_tokens (
       id VARCHAR(36) NOT NULL,
       email VARCHAR(75) NOT NULL,
       token VARCHAR(75) NOT NULL,
       expires_at TIMESTAMP NOT NULL,
       PRIMARY KEY (id)
);

CREATE INDEX ix_password_reset_token_email ON password_reset_tokens (email);
