drop table if exists ra_restaurant;

drop table if exists ra_restaurant_menu_items;

drop table if exists tickets;

drop table if exists ticket_line_items;

create table if not exists ra_restaurant(
  id bigint not null auto_increment,
  name varchar(255),
  primary key(id)
) engine = InnoDB;

create table if not exists ra_restaurant_menu_items(
  id bigint not null auto_increment,
  restaurant_id bigint,
  name varchar(255),
  price decimal(20, 2),
  primary key(id),
  unique(restaurant_id, name)
) engine = InnoDB;

create table if not exists tickets(
  id bigint not null auto_increment,
  customer_id bigint,
  order_id bigint,
  restaurant_id bigint,
  status varchar(255),
  primary key(id)
) engine = InnoDB;

create table if not exists ticket_line_items(
  id bigint not null auto_increment,
  ticket_id bigint,
  menu_item_id bigint,
  quantity int,
  primary key(id)
) engine = InnoDB;
