package edu.java.bot.linktracker.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.linktracker.commands.Command;
import edu.java.bot.linktracker.commands.processors.UserCommandProcessor;
import edu.java.bot.linktracker.replies.processors.UserReplyProcessor;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkTrackerBot implements Bot {
    private final TelegramBot bot;
    private final UserReplyProcessor userReplyProcessor;
    private final UserCommandProcessor userCommandProcessor;

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        for (var update : updates) {
            var request = isReply(update)
                ? userReplyProcessor.process(update)
                : userCommandProcessor.process(update);
            execute(request);
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @PostConstruct
    @Override
    public void start() {
        setupMenu();
        bot.setUpdatesListener(this);
    }

    @Override
    public void close() {
        bot.removeGetUpdatesListener();
    }

    private void setupMenu() {
        BotCommand[] botCommands = userCommandProcessor.commands()
            .stream()
            .map(Command::toApiCommand)
            .toArray(BotCommand[]::new);

        bot.execute(new SetMyCommands(botCommands));
    }

    private boolean isReply(Update update) {
        return update.message().replyToMessage() != null;
    }
}
