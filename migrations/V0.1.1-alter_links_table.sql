--liquibase formatted sql

--changeset shadi777:2
ALTER TABLE link ALTER COLUMN last_modified SET DEFAULT now();
