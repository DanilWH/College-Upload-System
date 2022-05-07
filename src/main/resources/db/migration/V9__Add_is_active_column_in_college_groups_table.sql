ALTER TABLE college_groups
    ADD COLUMN is_active boolean NOT NULL;

UPDATE college_groups
    SET is_active = true;