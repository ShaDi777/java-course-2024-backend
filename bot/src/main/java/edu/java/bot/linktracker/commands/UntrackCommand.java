package edu.java.bot.linktracker.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    @Override
    public String command() {
        return CommandConstants.UNTRACK_COMMAND;
    }

    @Override
    public String description() {
        return CommandConstants.UNTRACK_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(
            update.message().chat().id(),
            CommandConstants.UNTRACK_REPLY_TEXT
        ).replyMarkup(new ForceReply());
    }
}
