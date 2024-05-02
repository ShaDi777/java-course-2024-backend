package edu.java.bot.linktracker.replies.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.linktracker.commands.CommandConstants;
import edu.java.bot.linktracker.replies.Reply;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("BasicUserReplyProcessor")
@RequiredArgsConstructor
public class BasicUserReplyProcessor implements UserReplyProcessor {
    private final List<? extends Reply> replies;

    @Override
    public List<? extends Reply> replies() {
        return replies;
    }

    @Override
    public SendMessage process(Update update) {
        for (var reply : replies) {
            if (reply.supports(update)) {
                return reply.handle(update);
            }
        }

        return new SendMessage(update.message().chat().id(), CommandConstants.UNSUPPORTED_TEXT);
    }
}
