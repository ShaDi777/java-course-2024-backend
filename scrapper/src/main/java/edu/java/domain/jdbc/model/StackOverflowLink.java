package edu.java.domain.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StackOverflowLink {
    private Long linkId;

    private Integer commentsCount;

    private Integer answersCount;

    private Boolean isAnswered;

    public static StackOverflowLink parseStackOverflowLinkRowSet(ResultSet rs) throws SQLException {
        return StackOverflowLink.builder()
            .linkId(rs.getLong("link_id"))
            .commentsCount(rs.getInt("comments_count"))
            .answersCount(rs.getInt("answers_count"))
            .isAnswered(rs.getBoolean("is_answered"))
            .build();
    }
}
