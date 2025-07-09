-- liquibase formatted sql

-- changeset Admin:1752041105209-1
ALTER TABLE comments
    ALTER COLUMN ad_id SET NOT NULL;

-- changeset Admin:1752041105209-2
ALTER TABLE comments
    ALTER COLUMN author_id SET NOT NULL;

