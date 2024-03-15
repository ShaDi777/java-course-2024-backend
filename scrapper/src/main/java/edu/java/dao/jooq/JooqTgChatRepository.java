package edu.java.dao.jooq;

import edu.java.dao.jooq.generated.tables.pojos.Chat;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JooqTgChatRepository {
    private final DSLContext dsl;
    private final edu.java.dao.jooq.generated.tables.Chat chatTable = edu.java.dao.jooq.generated.tables.Chat.CHAT;

    public Optional<Chat> getById(Long chatId) {
        return dsl.selectFrom(chatTable)
            .where(chatTable.CHAT_ID.eq(chatId))
            .fetchOptionalInto(Chat.class);
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
