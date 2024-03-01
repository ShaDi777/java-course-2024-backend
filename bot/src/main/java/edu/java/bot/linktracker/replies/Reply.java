package edu.java.bot.linktracker.replies;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Reply {
    String reply();

    SendMessage handle(Update update);

    default boolean supports(Update update) {
        return update.message().replyToMessage() != null
            && update.message().replyToMessage().text().equals(reply());
    }
}
