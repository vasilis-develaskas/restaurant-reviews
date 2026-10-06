ALTER TABLE app_user DROP CONSTRAINT app_user_username_check;

ALTER TABLE app_user
    ADD CONSTRAINT app_user_username_check CHECK (username ~ '^[A-Za-z0-9._-]{3,30}$');