package edu.java.bot.linktracker.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.linktracker.chats.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final ChatRepository chatRepository;

    @Override
    public String command() {
        return CommandConstants.START_COMMAND;
    }

    @Override
    public String description() {
        return CommandConstants.START_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        chatRepository.registerChat(update.message().chat().id());
        return new SendMessage(update.message().chat().id(), CommandConstants.START_REPLY_TEXT);
    }
}
