drop table if exists ra_restaurant;

drop table if exists ra_restaurant_menu_items;

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