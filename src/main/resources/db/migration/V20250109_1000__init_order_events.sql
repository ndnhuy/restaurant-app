drop table if exists order_events;

create table if not exists order_events(
  id bigint not null auto_increment,
  order_id bigint,
  customer_id bigint,
  amount decimal(10, 2),
  status varchar(255),

  primary key(id)
) engine = InnoDB;
