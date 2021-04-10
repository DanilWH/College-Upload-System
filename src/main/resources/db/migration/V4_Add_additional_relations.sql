create table college_groups (id bigint not null,
    name varchar(255),
    primary key (id)
) engine=InnoDB;

create table students_results (id bigint not null,
    date_time datetime(6),
    filename varchar(255),
    task_id bigint,
    user_id bigint,
    primary key (id)
) engine=InnoDB;

create table tasks (id bigint not null,
    name varchar(255),
    group_id bigint,
    primary key (id)
) engine=InnoDB;

alter table users add column group_id bigint;
alter table students_results add constraint students_results_tasks_fk foreign key (task_id) references tasks (id);
alter table students_results add constraint students_results_users_fk foreign key (user_id) references users (id);
alter table tasks add constraint tasks_college_groups_fk foreign key (group_id) references college_groups (id);
alter table users add constraint users_college_groups_fk foreign key (group_id) references college_groups (id);
