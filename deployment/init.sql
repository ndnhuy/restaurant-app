create database if not exists ra;

create table ra_order(
  id bigint not null auto_increment,
  status varchar(255)
  primary key(`id`)
) engine = InnoDB;