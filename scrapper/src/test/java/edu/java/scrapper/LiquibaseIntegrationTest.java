package edu.java.scrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

public class LiquibaseIntegrationTest extends IntegrationTest {

    @Test
    public void linkTableTest() throws Exception {
        Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        );
        connection.setAutoCommit(false);

        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO link (url) VALUES ('https://github.com')");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM link");

        String actualUrl = null;
        while (resultSet.next()) {
            actualUrl = resultSet.getString("url");
        }

        connection.rollback();
        assertThat(actualUrl).isNotNull().isEqualTo("https://github.com");
    }

    @Test
    public void chatTableTest() throws Exception {
        Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        );
        connection.setAutoCommit(false);

        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO chat (chat_id) VALUES (123)");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM chat");

        Long actualId = null;
        while (resultSet.next()) {
            actualId = resultSet.getLong("chat_id");
        }

        connection.rollback();
        assertThat(actualId).isNotNull().isEqualTo(123);
    }

    @Test
    public void createTablesOnSetup() throws SQLException {
        try (Connection connection = DriverManager.getConnection(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
        )) {
            assertThat(tableExists(connection, "link")).isTrue();
            assertThat(tableExists(connection, "chat")).isTrue();
            assertThat(tableExists(connection, "link_chat")).isTrue();
        }
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        ResultSet rs = connection.getMetaData().getTables(null, null, tableName, new String[] {"TABLE"});
        return rs.next();
    }
}
