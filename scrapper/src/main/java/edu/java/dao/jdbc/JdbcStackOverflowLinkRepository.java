package edu.java.dao.jdbc;

import edu.java.dao.model.StackOverflowLink;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcStackOverflowLinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public Optional<StackOverflowLink> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                "SELECT * FROM link_stackoverflow WHERE link_id = ?",
                (rs, rowNum) -> parseStackOverflowLinkRowSet(rs),
                id
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public void upsertLink(StackOverflowLink link) {
        jdbcTemplate.update(
            """
                INSERT INTO link_stackoverflow(link_id, comments_count, answers_count, is_answered)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (link_id) DO UPDATE
                SET comments_count = ?, answers_count = ?, is_answered = ?
                """,
            link.getLinkId(), link.getCommentsCount(), link.getAnswersCount(), link.getIsAnswered(),
            link.getCommentsCount(), link.getAnswersCount(), link.getIsAnswered()
        );
    }

    private StackOverflowLink parseStackOverflowLinkRowSet(ResultSet rs) throws SQLException {
        return StackOverflowLink.builder()
            .linkId(rs.getLong("link_id"))
            .commentsCount(rs.getInt("comments_count"))
            .answersCount(rs.getInt("answers_count"))
            .isAnswered(rs.getBoolean("is_answered"))
            .build();
    }
}

