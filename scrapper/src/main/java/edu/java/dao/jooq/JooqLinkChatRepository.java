package edu.java.dao.jooq;

import edu.java.dao.jooq.generated.tables.pojos.LinkChat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JooqLinkChatRepository {
    private final DSLContext dsl;
    private final edu.java.dao.jooq.generated.tables.LinkChat linkChatTable =
        edu.java.dao.jooq.generated.tables.LinkChat.LINK_CHAT;

    public List<LinkChat> findAll() {
        return dsl.selectFrom(linkChatTable)
            .fetchInto(LinkChat.class);
    }

    public Optional<LinkChat> findAllByChatId(Long chatId) {
        return dsl.selectFrom(linkChatTable)
            .where(linkChatTable.CHAT_ID.eq(chatId))
            .fetchOptionalInto(LinkChat.class);
    }

    public Optional<LinkChat> findAllByLinkId(long id) {
        return dsl.selectFrom(linkChatTable)
            .where(linkChatTable.LINK_ID.eq(id))
            .fetchOptionalInto(LinkChat.class);
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
}
