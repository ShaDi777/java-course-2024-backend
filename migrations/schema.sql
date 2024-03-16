--liquibase formatted sql

--changeset shadi777:1
CREATE TABLE chat (
    chat_id BIGINT PRIMARY KEY
);

CREATE TABLE link (
    link_id BIGSERIAL PRIMARY KEY,
    url TEXT UNIQUE NOT NULL,
    last_modified TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT TIMESTAMP 'epoch',
    last_checked TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT TIMESTAMP 'epoch'
);

CREATE TABLE link_chat (
     link_id BIGINT NOT NULL REFERENCES link (link_id),
     chat_id BIGINT NOT NULL REFERENCES chat (chat_id),
     PRIMARY KEY(link_id, chat_id)
);

--changeset shadi777:2
ALTER TABLE link ALTER COLUMN last_modified SET DEFAULT now();

--changeset shadi777:3
CREATE TABLE link_stackoverflow (
    link_id BIGINT PRIMARY KEY REFERENCES link (link_id) ON DELETE CASCADE,
    comments_count INT DEFAULT 0,
    answers_count INT DEFAULT 0,
    is_answered BOOLEAN DEFAULT FALSE
);
