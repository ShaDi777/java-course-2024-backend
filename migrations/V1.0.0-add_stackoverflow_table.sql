--liquibase formatted sql

--changeset shadi777:3
CREATE TABLE link_stackoverflow (
    link_id BIGINT PRIMARY KEY REFERENCES link (link_id) ON DELETE CASCADE,
    comments_count INT DEFAULT 0,
    answers_count INT DEFAULT 0,
    is_answered BOOLEAN DEFAULT FALSE
);
