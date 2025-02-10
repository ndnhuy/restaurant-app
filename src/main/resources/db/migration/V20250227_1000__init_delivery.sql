drop table if exists delivery;

create table if not exists delivery(
  id bigint not null auto_increment,
  order_id bigint,
  customer_id bigint,
  restaurant_id bigint,
  assigned_courier_id bigint,
  ready_by datetime,
  shipping_amount decimal(10, 2),
  status varchar(255),

  primary key(id)
) engine = InnoDB;
