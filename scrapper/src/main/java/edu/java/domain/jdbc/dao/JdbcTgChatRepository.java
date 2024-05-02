package edu.java.domain.jdbc.dao;

import edu.java.domain.jdbc.model.TgChat;
import edu.java.exceptions.ChatAlreadyExistsException;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcTgChatRepository {
    private final JdbcTemplate jdbcTemplate;

    public TgChat save(TgChat tgChat) {
        int affectedRows =
            jdbcTemplate.update("INSERT INTO chat VALUES (?) ON CONFLICT DO NOTHING", tgChat.getChatId());
        if (affectedRows == 0) {
            throw new ChatAlreadyExistsException();
        }

        return tgChat;
    }

    public TgChat deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM chat WHERE chat_id = ?", id);
        return TgChat.builder().chatId(id).build();
    }

    public Optional<TgChat> findById(long chatId) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                    "SELECT * FROM chat WHERE chat_id = ?",
                    (rs, rowNum) -> TgChat.parseResultSet(rs),
                    chatId
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Collection<TgChat> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM chat",
            (rs, rowNum) -> TgChat.parseResultSet(rs)
        );
    }
}
