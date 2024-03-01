package edu.java.bot.linktracker.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    private final String helpText;

    @Autowired
    public HelpCommand(List<Command> commands) {
        var stringBuilder = new StringBuilder();
        for (var command : commands) {
            addCommandText(stringBuilder, command);
        }
        addCommandText(stringBuilder, this);
        this.helpText = stringBuilder.toString();
    }

    @Override
    public String command() {
        return CommandConstants.HELP_COMMAND;
    }

    @Override
    public String description() {
        return CommandConstants.HELP_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), helpText);
    }

    private void addCommandText(StringBuilder stringBuilder, Command command) {
        stringBuilder.append(command.command());
        stringBuilder.append(" - ");
        stringBuilder.append(command.description());
        stringBuilder.append('\n');
    }
}
