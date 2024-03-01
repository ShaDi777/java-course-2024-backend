package edu.java.bot.linktracker.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    @Override
    public String command() {
        return CommandConstants.TRACK_COMMAND;
    }

    @Override
    public String description() {
        return CommandConstants.TRACK_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(
            update.message().chat().id(),
            CommandConstants.TRACK_REPLY_TEXT
        ).replyMarkup(new ForceReply());
    }
}
