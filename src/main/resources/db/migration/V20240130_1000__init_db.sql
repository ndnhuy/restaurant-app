create table if not exists ra_order(
  id bigint not null auto_increment,
  customer_id bigint,
  status varchar(255),
  amount decimal(10, 2),
  primary key(`id`)
) engine = InnoDB;