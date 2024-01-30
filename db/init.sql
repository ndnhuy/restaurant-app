create database if not exists ra;

create table order(
  id bigint not null auto_increment,
  status varchar(255)
) engine = InnoDB;