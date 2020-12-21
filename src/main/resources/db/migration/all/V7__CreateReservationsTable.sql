CREATE TABLE reservations (
    id VARCHAR(36) NOT NULL,
    restaurant_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36),
    number_of_people INT DEFAULT 1,
    datetime TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_restaurant
        FOREIGN KEY(restaurant_id)
            REFERENCES restaurants(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
            REFERENCES users(id)
            ON DELETE SET NULL
);