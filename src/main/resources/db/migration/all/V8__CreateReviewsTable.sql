CREATE TABLE reviews (
      id VARCHAR(36) NOT NULL,
      restaurant_id VARCHAR(36) NOT NULL,
      reviewer_id VARCHAR(36),
      rating INT NOT NULL,
      comment TEXT,
      created_at TIMESTAMP,
      updated_at TIMESTAMP,
      PRIMARY KEY (id),
      CONSTRAINT fk_restaurant
          FOREIGN KEY(restaurant_id)
              REFERENCES restaurants(id)
              ON DELETE CASCADE,
      CONSTRAINT fk_user
          FOREIGN KEY(reviewer_id)
              REFERENCES users(id)
              ON DELETE SET NULL
);