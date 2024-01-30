create table if not exists ra_order(
  id bigint not null auto_increment,
  status varchar(255),
  primary key(`id`)
) engine = InnoDB;