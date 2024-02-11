package edu.java.bot.linktracker.replies.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.linktracker.commands.CommandConstants;
import edu.java.bot.linktracker.links.LinkRepository;
import edu.java.bot.linktracker.replies.Reply;
import edu.java.bot.linktracker.replies.TrackReply;
import edu.java.bot.linktracker.replies.UntrackReply;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("BasicUserReplyProcessor")
public class BasicUserReplyProcessor implements UserReplyProcessor {
    private final List<? extends Reply> replies;

    public BasicUserReplyProcessor(@Autowired LinkRepository linkRepository) {
        replies = List.of(
            new TrackReply(linkRepository),
            new UntrackReply(linkRepository)
        );
    }

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
