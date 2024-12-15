drop table if exists ra_payment_order;

create table if not exists ra_payment_order(
  id bigint not null auto_increment,
  order_id bigint,
  customer_id bigint,
  amount decimal(10, 2),
  status varchar(255),

  primary key(id)
) engine = InnoDB;
