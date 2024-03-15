package edu.java.dao.jdbc;

import edu.java.dao.model.Link;
import edu.java.dao.model.TgChat;
import edu.java.exceptions.ResourceNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public Link add(Long chatId, String linkUrl) {
        TgChat chat;
        try {
            chat = jdbcTemplate.queryForObject(
                "SELECT * FROM chat WHERE chat_id = ?",
                (rs, rowNum) -> parseTgChatRowSet(rs),
                chatId
            );
        } catch (EmptyResultDataAccessException e) {
            throw ResourceNotFoundException.chatNotFound(chatId);
        }

        jdbcTemplate.update(
            """
                    INSERT INTO link (url)
                    SELECT ?
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM link
                        WHERE url = ?
                    );
                """, linkUrl, linkUrl);
        Link link = jdbcTemplate.queryForObject(
            "SELECT * FROM link WHERE url = ? LIMIT 1",
            (rs, rowNum) -> parseLinkRowSet(rs),
            linkUrl
        );

        jdbcTemplate.update(
            """
                    INSERT INTO link_chat (chat_id, link_id)
                    SELECT ?, ?
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM link_chat
                        WHERE chat_id = ? AND link_id = ?
                    );
                """, chatId, link.getLinkId(), chatId, link.getLinkId());
        return link;
    }

    @Transactional
    public Link remove(Long chatId, String linkUrl) {
        Link link;
        try {
            link = jdbcTemplate.queryForObject(
                "SELECT * FROM link where url = ?",
                (rs, rowNum) -> parseLinkRowSet(rs),
                linkUrl
            );
        } catch (EmptyResultDataAccessException e) {
            throw ResourceNotFoundException.linkNotFound(chatId, linkUrl);
        }

        int deletedRows =
            jdbcTemplate.update("DELETE FROM link_chat WHERE link_id = ? AND chat_id = ?", link.getLinkId(), chatId);

        Long countChatListeners = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM link_chat WHERE link_id = ?",
            Long.class,
            link.getLinkId()
        );

        if (countChatListeners == null || countChatListeners == 0L) {
            jdbcTemplate.update("DELETE FROM link WHERE link_id = ?", link.getLinkId());
        }

        if (deletedRows == 0) {
            throw ResourceNotFoundException.linkNotFound(chatId, linkUrl);
        }

        return link;
    }

    @Transactional
    public Link findById(Long linkId) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM link where link_id = ?",
                (rs, rowNum) -> parseLinkRowSet(rs),
                linkId
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional
    public Collection<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM link", (rs, rowNum) -> parseLinkRowSet(rs));
    }

    @Transactional
    public Collection<Link> findAllLinksByChatId(Long chatId) {
        return jdbcTemplate.query(
            "SELECT * FROM link WHERE link_id IN (SELECT link_id FROM link_chat WHERE chat_id = ?)",
            (rs, rowNum) -> parseLinkRowSet(rs), chatId
        );
    }

    @Transactional
    public Collection<TgChat> findAllChatsByLinkId(Long linkId) {
        return jdbcTemplate.query(
            "SELECT chat_id FROM link_chat WHERE link_id = ?",
            (rs, rowNum) -> parseTgChatRowSet(rs), linkId
        );
    }

    @Transactional
    public Collection<Link> findNLinksByOldestLastCheck(int count) {
        return jdbcTemplate.query(
            "SELECT * FROM link ORDER BY last_checked LIMIT ?",
            (rs, rowNum) -> parseLinkRowSet(rs),
            count
        );
    }

    @Transactional
    public void updateLastModified(long linkId, OffsetDateTime offsetDateTime) {
        jdbcTemplate.update("UPDATE link SET last_modified = ? WHERE link_id = ?", offsetDateTime, linkId);
    }

    @Transactional
    public void updateLastChecked(long linkId, OffsetDateTime offsetDateTime) {
        jdbcTemplate.update("UPDATE link SET last_checked = ? WHERE link_id = ?", offsetDateTime, linkId);
    }

    private Link parseLinkRowSet(ResultSet rs) throws SQLException {
        return Link.builder()
            .linkId(rs.getLong("link_id"))
            .url(rs.getString("url"))
            .lastModified(rs.getObject("last_modified", OffsetDateTime.class))
            .lastChecked(rs.getObject("last_checked", OffsetDateTime.class))
            .build();
    }

    private TgChat parseTgChatRowSet(ResultSet rs) throws SQLException {
        return TgChat.builder().chatId(rs.getLong("chat_id")).build();
    }
}

