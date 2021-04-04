insert into users (id, first_name, last_name, login, password) values (
    0,
    'Igor',
    'Anatolevich',
    'admin',
    '$2y$12$rR7yaX6pbAN/glad6bBypuShvyAq3SS9RMphujBs99Yq8f2hK5AaW'
);

insert into user_roles (user_id, user_roles) values (0, 'ADMIN');
update hibernate_sequence set next_val = 1;
