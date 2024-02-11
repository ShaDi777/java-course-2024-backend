package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.linktracker.bot.LinkTrackerBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
    private final static Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        var context = SpringApplication.run(BotApplication.class, args);
        ApplicationConfig config = context.getBean(ApplicationConfig.class);
        LOGGER.info(config);

        var bot = context.getBean(LinkTrackerBot.class);
        bot.start();
    }
}
