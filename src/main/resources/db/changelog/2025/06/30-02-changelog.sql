-- liquibase formatted sql

-- changeset Admin:1751256221368-1
ALTER TABLE users
    ADD image_id UUID;

-- changeset Admin:1751256221368-2
ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_IMAGE FOREIGN KEY (image_id) REFERENCES images (id);

-- changeset Admin:1751256221368-3
ALTER TABLE users
    DROP COLUMN image;

