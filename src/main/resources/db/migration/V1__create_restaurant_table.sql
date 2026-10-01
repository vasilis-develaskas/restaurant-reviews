CREATE TABLE restaurant
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                VARCHAR(100) NOT NULL,
    description         TEXT,
    price_range         INT NOT NULL CHECK (price_range >= 1 and price_range <= 5),
    address             VARCHAR(200) NOT NULL,
    city                VARCHAR(100) NOT NULL,
    neighborhood        VARCHAR(100) NOT NULL,
    phone               VARCHAR(20),
    website             VARCHAR(200),
    established_year    INTEGER,
    review_count        INTEGER     NOT NULL DEFAULT 0 CHECK (review_count >= 0),
    rating_sum          BIGINT      NOT NULL DEFAULT 0 CHECK (rating_sum >= 0),
    version             BIGINT      NOT NULL DEFAULT 0,
    created_at          TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ   NOT NULL DEFAULT now()
);