# --- First database schema
 
# --- !Ups

CREATE SEQUENCE nav_tem_id_seq;

CREATE TABLE hd_nav_item (
    id integer NOT NULL DEFAULT nextval('nav_tem_id_seq') primary key,
    group_name varchar(100) not null,
    item varchar(500) not null,
    relative_url varchar(2000) not null,
    group_num INT not null
    
);
 
# --- !Downs
 
DROP TABLE hd_nav_item;
DROP SEQUENCE nav_tem_id_seq;
