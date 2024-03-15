package edu.java.dao.jdbc;

import edu.java.dao.model.TgChat;
import edu.java.exceptions.ChatAlreadyExistsException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcTgChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public TgChat createById(Long id) {
        int affectedRows = jdbcTemplate.update("INSERT INTO chat VALUES (?) ON CONFLICT DO NOTHING", id);
        if (affectedRows == 0) {
            throw new ChatAlreadyExistsException();
        }

        return TgChat.builder().chatId(id).build();
    }

    @Transactional
    public TgChat deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM link_chat WHERE chat_id = ?", id);
        jdbcTemplate.update("""
            DELETE FROM link
            WHERE link_id NOT IN (
                SELECT DISTINCT link_id
                FROM link_chat
            )
            """);
        jdbcTemplate.update("DELETE FROM chat WHERE chat_id = ?", id);
        return TgChat.builder().chatId(id).build();
    }

    @Transactional
    public Collection<TgChat> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM chat",
            (rs, rowNum) -> TgChat.builder().chatId(rs.getLong("chat_id")).build()
        );
    }
}
