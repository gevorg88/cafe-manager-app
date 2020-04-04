
create table if not exists orders
(
    id       bigint not null auto_increment,
    status   varchar(255) not null,
    table_id bigint not null,
    primary key (id)
) engine = InnoDB;

create table if not exists products
(
    id   bigint not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine = InnoDB;

create table if not exists products_in_order
(
    id        bigint not null auto_increment,
    amount     integer not null,
    order_id   bigint not null,
    product_id bigint not null,
    primary key (id)
) engine = InnoDB;

create table if not exists cafe_tables
(
    id      bigint not null auto_increment,
    name    varchar(255) not null,
    user_id bigint,
    primary key (id)
) engine = InnoDB;

create table if not exists users
(
    id         bigint       not null auto_increment,
    email      varchar(255) not null,
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255) not null ,
    role       varchar(255) not null,
    username   varchar(255) not null,
    primary key (id)
) engine = InnoDB;

create index idx_user_email on users (`email`);

create index idx_user_username on users (`username`);

create index idx_cafe_table_name on cafe_tables (`name`);

create index idx_product_name on products (`name`);