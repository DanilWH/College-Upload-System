create table college_groups (
    id bigint not null,
    name varchar(255),
    primary key (id)
) engine=InnoDB;

create table hibernate_sequence (
    next_val bigint
) engine=InnoDB;

insert into hibernate_sequence values ( 1 );

create table students_results (
    id bigint not null,
    date_time datetime(6),
    filename varchar(255),
    filepath varchar(510),
    task_id bigint,
    user_id bigint,
    primary key (id)
) engine=InnoDB;

create table tasks (
    id bigint not null,
    name varchar(20),
    group_id bigint,
    primary key (id)
) engine=InnoDB;

create table user_roles (
    user_id bigint not null,
    user_roles varchar(20)
) engine=InnoDB;

create table users (
    id bigint not null,
    creation_time datetime(6),
    father_name varchar(30),
    first_name varchar(30),
    last_name varchar(30),
    login varchar(20),
    password varchar(100),
    password_change_time datetime(6),
    group_id bigint,
    password_changer_id bigint,
    user_creator_id bigint,
    primary key (id)
) engine=InnoDB;

alter table students_results add constraint students_results_fk_task_id foreign key (task_id) references tasks (id);
alter table students_results add constraint students_results_fk_user_id foreign key (user_id) references users (id);
alter table tasks add constraint tasks_fk_college_group_id foreign key (group_id) references college_groups (id);
alter table user_roles add constraint user_roles_fk_user_id foreign key (user_id) references users (id);
alter table users add constraint users_fk_college_group_id foreign key (group_id) references college_groups (id);
alter table users add constraint users_fk_password_changer_id foreign key (password_changer_id) references users (id);
alter table users add constraint users_fk_user_creator_id foreign key (user_creator_id) references users (id);
