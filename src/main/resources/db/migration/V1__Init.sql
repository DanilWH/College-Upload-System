drop table if exists hibernate_sequence;
drop table if exists user_roles;
drop table if exists users;
create table hibernate_sequence (next_val bigint) engine=InnoDB;
insert into hibernate_sequence values ( 1 );

create table user_roles (
    user_id bigint not null,
    user_roles varchar(255)
) engine=InnoDB;

create table users (
    id bigint not null,
    first_name varchar(255),
    last_name varchar(255),
    login varchar(255),
    password varchar(255),
    primary key (id)
) engine=InnoDB;

alter table user_roles add constraint user_role_fk foreign key (user_id) references users (id);
