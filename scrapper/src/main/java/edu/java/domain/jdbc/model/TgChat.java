package edu.java.domain.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TgChat {
    private Long chatId;

    public static TgChat parseResultSet(ResultSet rs) throws SQLException {
        return TgChat.builder().chatId(rs.getLong("chat_id")).build();
    }
}
