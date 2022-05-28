UPDATE users
    SET creation_time = now(),
        password_change_time = creation_time,
        password_changer_id = 1,
        user_creator_id = 1
    WHERE id = 1;