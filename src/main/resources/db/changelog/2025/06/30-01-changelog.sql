-- liquibase formatted sql

-- changeset Admin:1751255642851-1
CREATE TABLE images
(
    id         UUID   NOT NULL,
    file_path  VARCHAR(255),
    file_size  BIGINT NOT NULL,
    media_type VARCHAR(255),
    CONSTRAINT pk_images PRIMARY KEY (id)
);

