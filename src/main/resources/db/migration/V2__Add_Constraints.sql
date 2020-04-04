alter table products
    add constraint unique_products_name unique (`name`);

alter table users
    add constraint unique_users_email unique (`email`);

alter table users
    add constraint unique_users_username unique (`username`);

alter table orders
    add constraint orders_to_cafe_tables_fk
        foreign key (`table_id`)
            references cafe_tables (id)
            ON DELETE CASCADE ON UPDATE NO ACTION;

alter table products_in_order
    add constraint products_in_order_to_orders_fk
        foreign key (`order_id`)
            references orders (id)
            ON DELETE CASCADE ON UPDATE NO ACTION;

alter table products_in_order
    add constraint products_in_order_to_products_fk
        foreign key (`product_id`)
            references products (id)
            ON DELETE CASCADE ON UPDATE NO ACTION;

alter table cafe_tables
    add constraint cafe_tables_to_users_fk
        foreign key (`user_id`)
            references users (id);