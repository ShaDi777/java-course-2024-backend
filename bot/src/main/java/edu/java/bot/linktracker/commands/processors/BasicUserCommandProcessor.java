package edu.java.bot.linktracker.commands.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.linktracker.commands.Command;
import edu.java.bot.linktracker.commands.CommandConstants;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("BasicUserCommandProcessor")
public class BasicUserCommandProcessor implements UserCommandProcessor {
    private final List<? extends Command> commands;
    private final Counter meterCounter;

    @Autowired
    public BasicUserCommandProcessor(List<? extends Command> commands, MeterRegistry meterRegistry) {
        this.commands = commands;
        this.meterCounter = meterRegistry.counter("message_processed_count");
    }

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        meterCounter.increment();

        for (var command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }

        return new SendMessage(update.message().chat().id(), CommandConstants.UNSUPPORTED_TEXT);
    }
}
