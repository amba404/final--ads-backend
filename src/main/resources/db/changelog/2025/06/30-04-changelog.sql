-- liquibase formatted sql

-- changeset Admin:1751268842632-1
ALTER TABLE ads
    ALTER COLUMN author_id SET NOT NULL;

