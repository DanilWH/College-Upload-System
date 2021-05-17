INSERT INTO users (id, first_name, last_name, father_name, login, password) VALUES (
    1,
    'Игорь',
    'Шашин',
    'Анатольевич',
    'admin',
    '$2y$12$7tIlQDyGq7vQy/3kbBMX0OOwfgwjuxCniN8bt8DKlpoEO63CzaFXW'
);

INSERT INTO user_roles (user_id, user_roles)
    VALUES (1, 'ADMIN');

UPDATE hibernate_sequence SET next_val = 2;