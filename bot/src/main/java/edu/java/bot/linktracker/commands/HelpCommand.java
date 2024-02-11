package edu.java.bot.linktracker.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommand implements Command {
    private final String helpText;

    public HelpCommand(Command... commands) {
        var strBuilder = new StringBuilder();
        for (var command : commands) {
            strBuilder.append(command.command());
            strBuilder.append(" - ");
            strBuilder.append(command.description());
            strBuilder.append('\n');
        }
        helpText = strBuilder.toString();
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
}
