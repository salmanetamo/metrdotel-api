CREATE TABLE order_items (
     id VARCHAR(36) NOT NULL,
     order_id VARCHAR(36) NOT NULL,
     menu_item_id VARCHAR(36),
     quantity INT,
     PRIMARY KEY (id),
     CONSTRAINT fk_order
         FOREIGN KEY(order_id)
             REFERENCES orders(id)
             ON DELETE CASCADE,
     CONSTRAINT fk_menu_item
         FOREIGN KEY(menu_item_id)
             REFERENCES menu_items(id)
             ON DELETE SET NULL
);