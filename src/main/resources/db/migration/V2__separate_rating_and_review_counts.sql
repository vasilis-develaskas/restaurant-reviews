ALTER TABLE restaurant RENAME COLUMN review_count TO rating_count;

ALTER TABLE restaurant RENAME CONSTRAINT restaurant_review_count_check TO restaurant_rating_count_check;

ALTER TABLE restaurant ADD COLUMN review_count INTEGER NOT NULL DEFAULT 0 CHECK (review_count >= 0);