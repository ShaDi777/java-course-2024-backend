package edu.java.domain.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Link {
    private Long linkId;

    private String url;

    private OffsetDateTime lastModified;

    private OffsetDateTime lastChecked;

    public static Link parseResultSet(ResultSet rs) throws SQLException {
        return Link.builder()
            .linkId(rs.getLong("link_id"))
            .url(rs.getString("url"))
            .lastModified(rs.getObject("last_modified", OffsetDateTime.class))
            .lastChecked(rs.getObject("last_checked", OffsetDateTime.class))
            .build();
    }
}
