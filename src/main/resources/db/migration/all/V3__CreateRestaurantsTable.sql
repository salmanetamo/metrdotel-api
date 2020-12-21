CREATE TABLE restaurants (
   id VARCHAR(36) NOT NULL,
   name VARCHAR(100) NOT NULL,
   type VARCHAR(50) NOT NULL,
   cover_image VARCHAR(75),
   opening_hours JSON,
   amenities TEXT [],
   price_range INT,
   description TEXT,
   location JSON,
   created_at TIMESTAMP,
   updated_at TIMESTAMP,
   PRIMARY KEY (id)
);