ALTER TABLE users
    MODIFY first_name varchar(50),
    MODIFY last_name varchar(50),
    MODIFY father_name varchar(50),

    MODIFY login varchar(60),
    MODIFY password varchar(60); /* as a BCrypt hash total length is 59 or 60. */
