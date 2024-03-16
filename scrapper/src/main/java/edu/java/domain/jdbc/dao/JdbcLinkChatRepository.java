package edu.java.domain.jdbc.dao;

import edu.java.domain.jdbc.model.Link;
import edu.java.domain.jdbc.model.LinkChat;
import edu.java.domain.jdbc.model.TgChat;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkChatRepository {
    private final JdbcTemplate jdbcTemplate;

    public LinkChat save(LinkChat linkChat) {
        jdbcTemplate.update(
            "INSERT INTO link_chat (link_id, chat_id) VALUES(?, ?) ON CONFLICT DO NOTHING",
            linkChat.getLinkId(), linkChat.getChatId()
        );
        return linkChat;
    }

    public int deleteByIds(long linkId, long chatId) {
        return jdbcTemplate.update(
            "DELETE FROM link_chat WHERE link_id = ? AND chat_id = ?",
            linkId, chatId
        );
    }

    public int deleteAllByChatId(long chatId) {
        return jdbcTemplate.update(
            "DELETE FROM link_chat WHERE chat_id = ?",
            chatId
        );
    }

    public int deleteAllByLinkId(long linkId) {
        return jdbcTemplate.update(
            "DELETE FROM link_chat WHERE linkId = ?",
            linkId
        );
    }

    public Collection<Link> findAllLinksByChatId(Long chatId) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE link_id IN (SELECT link_id FROM link_chat WHERE chat_id = ?)",
            (rs, rowNum) -> Link.parseResultSet(rs),
            chatId
        );
    }

    public Collection<TgChat> findAllChatsByLinkId(Long linkId) {
        return jdbcTemplate.query(
            "SELECT chat_id FROM link_chat WHERE link_id = ?",
            (rs, rowNum) -> TgChat.parseResultSet(rs),
            linkId
        );
    }
}
