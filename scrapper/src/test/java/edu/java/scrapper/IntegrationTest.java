package edu.java.scrapper;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.ChangeExecListenerCommandStep;
import liquibase.command.core.helpers.DatabaseChangelogCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
public abstract class IntegrationTest {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    @SneakyThrows
    private static void runMigrations(JdbcDatabaseContainer<?> c) {
        try (Connection connection = DriverManager.getConnection(c.getJdbcUrl(), c.getUsername(), c.getPassword())) {
            Database database =
                DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            String scrapperProjectPath = new File("").getAbsolutePath();
            Path projectPath = new File(scrapperProjectPath).toPath().getParent();
            Path searchPath = projectPath.resolve("migrations");

            Liquibase liquibase = new liquibase.Liquibase(
                "master.xml",
                new DirectoryResourceAccessor(searchPath),
                database
            );

            var contexts = new Contexts();
            var labelExpression = new LabelExpression();

            CommandScope updateCommand = new CommandScope(UpdateCommandStep.COMMAND_NAME);
            updateCommand.addArgumentValue(DbUrlConnectionCommandStep.DATABASE_ARG, liquibase.getDatabase());
            updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_ARG, liquibase.getDatabaseChangeLog());
            updateCommand.addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, liquibase.getChangeLogFile());
            updateCommand.addArgumentValue(UpdateCommandStep.CONTEXTS_ARG, contexts.toString());
            updateCommand.addArgumentValue(UpdateCommandStep.LABEL_FILTER_ARG, labelExpression.getOriginalString());
            updateCommand.addArgumentValue(
                ChangeExecListenerCommandStep.CHANGE_EXEC_LISTENER_ARG,
                liquibase.getDefaultChangeExecListener()
            );
            updateCommand.addArgumentValue(
                DatabaseChangelogCommandStep.CHANGELOG_PARAMETERS,
                liquibase.getChangeLogParameters()
            );
            updateCommand.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.liquibase.enabled", () -> false);
        registry.add("app.scheduler.enable", () -> false);
        registry.add("app.use-queue", () -> false);
    }
}
