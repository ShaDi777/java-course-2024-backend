package edu.java.domain.jooq.dao;

import edu.java.domain.jooq.generated.tables.pojos.LinkChat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JooqLinkChatRepository {
    private final DSLContext dsl;
    private final edu.java.domain.jooq.generated.tables.LinkChat linkChatTable =
        edu.java.domain.jooq.generated.tables.LinkChat.LINK_CHAT;

    public List<LinkChat> findAll() {
        return dsl.selectFrom(linkChatTable)
            .fetchInto(LinkChat.class);
    }

    public List<LinkChat> findAllByChatId(long chatId) {
        return dsl.selectFrom(linkChatTable)
            .where(linkChatTable.CHAT_ID.eq(chatId))
            .fetchInto(LinkChat.class);
    }

    public List<LinkChat> findAllByLinkId(long linkId) {
        return dsl.selectFrom(linkChatTable)
            .where(linkChatTable.LINK_ID.eq(linkId))
            .fetchInto(LinkChat.class);
    }

    public int add(LinkChat newChatLink) {
        return dsl.insertInto(linkChatTable)
            .set(dsl.newRecord(linkChatTable, newChatLink))
            .execute();
    }

    public int delete(Long chatId, Long linkId) {
        return dsl.deleteFrom(linkChatTable)
            .where(linkChatTable.CHAT_ID.eq(chatId).and(linkChatTable.LINK_ID.eq(linkId)))
            .execute();
    }

    public int deleteAllByChatId(Long chatId) {
        return dsl.deleteFrom(linkChatTable)
            .where(linkChatTable.CHAT_ID.eq(chatId))
            .execute();
    }
}
