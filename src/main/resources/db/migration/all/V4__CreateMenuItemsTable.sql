CREATE TABLE menu_items (
    id VARCHAR(36) NOT NULL,
    restaurant_id VARCHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL,
    picture VARCHAR(75),
    price DECIMAL NOT NULL,
    description TEXT,
    types TEXT [],
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_restaurant
        FOREIGN KEY(restaurant_id)
            REFERENCES restaurants(id)
            ON DELETE CASCADE
);