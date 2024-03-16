package edu.java.domain.jdbc.dao;

import edu.java.domain.jdbc.model.Link;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository {
    private final JdbcTemplate jdbcTemplate;

    public Link save(Link link) {
        jdbcTemplate.update(
            """
                    INSERT INTO link (url)
                    SELECT ?
                    WHERE NOT EXISTS (
                        SELECT 1
                        FROM link
                        WHERE url = ?
                    );
                """, link.getUrl(), link.getUrl());
        return findByUrl(link.getUrl()).get();
    }

    public void updateLink(Link link) {
        jdbcTemplate.update(
            "UPDATE link SET last_modified = ?, last_checked = ? WHERE link_id = ?",
            link.getLastModified(), link.getLastChecked(), link.getLinkId()
        );
    }

    public int deleteById(long linkId) {
        return jdbcTemplate.update("DELETE FROM link WHERE link_id = ?", linkId);
    }

    public int deleteByUrl(String url) {
        return jdbcTemplate.update("DELETE FROM link WHERE url = ?", url);
    }

    public Optional<Link> findById(long linkId) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                    "SELECT * FROM link WHERE link_id = ?",
                    (rs, rowNum) -> Link.parseResultSet(rs),
                    linkId
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Link> findByUrl(String url) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                    "SELECT * FROM link WHERE url = ?",
                    (rs, rowNum) -> Link.parseResultSet(rs),
                    url
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Collection<Link> findAll() {
        return jdbcTemplate.query("SELECT * FROM link", (rs, rowNum) -> Link.parseResultSet(rs));
    }

    public Collection<Link> findNLinksByOldestLastCheck(int count) {
        return jdbcTemplate.query(
            "SELECT * FROM link ORDER BY last_checked LIMIT ?",
            (rs, rowNum) -> Link.parseResultSet(rs),
            count
        );
    }
}

