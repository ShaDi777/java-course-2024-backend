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
