CREATE TABLE app_user
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email               VARCHAR(255) NOT NULL UNIQUE CHECK (email = LOWER(email)),
    username            VARCHAR(30) NOT NULL CHECK (POSITION('@' IN username) = 0),
    password_hash       VARCHAR(100) NOT NULL,
    role                VARCHAR(20) NOT NULL DEFAULT 'USER' CHECK (role IN ('USER','ADMIN')),
    version             BIGINT      NOT NULL DEFAULT 0,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uq_app_user_username_lower ON app_user (LOWER(username));