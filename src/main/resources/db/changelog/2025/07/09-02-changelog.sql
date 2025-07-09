-- liquibase formatted sql

-- changeset Admin:1752041105210-1
ALTER TABLE comments
    DROP CONSTRAINT fk_comments_on_ad;

-- changeset Admin:1752041105210-2
ALTER TABLE comments
    ADD CONSTRAINT fk_comments_on_ad
        FOREIGN KEY (ad_id) REFERENCES ads(id)
            ON DELETE CASCADE;