package edu.java.domain.jooq.dao;

import edu.java.domain.jooq.generated.tables.pojos.Chat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JooqTgChatRepository {
    private final DSLContext dsl;
    private final edu.java.domain.jooq.generated.tables.Chat chatTable =
        edu.java.domain.jooq.generated.tables.Chat.CHAT;

    public Optional<Chat> getById(Long chatId) {
        return dsl.selectFrom(chatTable)
            .where(chatTable.CHAT_ID.eq(chatId))
            .fetchOptionalInto(Chat.class);
    }

    public List<Chat> getAll() {
        return dsl.selectFrom(chatTable)
            .fetchInto(Chat.class);
    }

    public int add(Chat newChat) {
        return dsl.insertInto(chatTable)
            .set(dsl.newRecord(chatTable, newChat))
            .execute();
    }

    public int deleteById(Long chatId) {
        return dsl.deleteFrom(chatTable)
            .where(chatTable.CHAT_ID.eq(chatId))
            .execute();
    }
}
