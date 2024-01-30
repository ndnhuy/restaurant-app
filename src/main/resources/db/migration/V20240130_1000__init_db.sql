create table if not exists ra_order(
  id bigint not null auto_increment,
  customer_id bigint,
  status varchar(255),
  primary key(`id`)
) engine = InnoDB;