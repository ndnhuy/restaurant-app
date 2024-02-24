drop table if exists ra_user;

drop table if exists ra_document;

create table ra_user(
  id bigint not null,
  name varchar(255),
  comment varchar(255),
  primary key(id)
) engine = InnoDB;

create table ra_document(
  id bigint not null,
  title varchar(255),
  user_id bigint,
  primary key(id)
) engine = InnoDB;

-- insert into "RA_USER" values (101, 'user1', 'comment1');
-- insert into "RA_USER" values (102, 'user2', 'comment2');
-- insert into "RA_USER" values (103, 'user3', 'comment3');
-- insert into "RA_USER" values (104, 'user4', 'comment4');
-- insert into "RA_USER" values (105, 'user5', 'comment5');

-- insert into "RA_DOCUMENT"  values (1, 'doc1', 101);
-- insert into "RA_DOCUMENT"  values (2, 'doc2', 101);
-- insert into "RA_DOCUMENT"  values (3, 'doc3', 101);
-- insert into "RA_DOCUMENT"  values (4, 'doc4', 101);
-- insert into "RA_DOCUMENT"  values (5, 'doc5', 102);
-- insert into "RA_DOCUMENT"  values (6, 'doc6', 102);

-- insert into "RA_RESTAURANT" values (1, 'test restaurant 1');
-- insert into "RA_RESTAURANT_MENU_ITEMS" values (1, 1, 'pizza type 1', 5);
-- insert into "RA_RESTAURANT_MENU_ITEMS" values (2, 1, 'pizza type 2', 10);