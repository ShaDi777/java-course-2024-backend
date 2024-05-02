package edu.java.bot.linktracker.replies;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.linktracker.commands.CommandConstants;
import edu.java.bot.linktracker.links.LinkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrackReply implements Reply {
    private final LinkRepository linkRepository;

    @Override
    public String reply() {
        return CommandConstants.TRACK_REPLY_TEXT;
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        String link = update.message().text();

        if (!linkRepository.isValidLink(link)) {
            return new SendMessage(chatId, ReplyConstants.TRACK_FAILURE_TEXT);
        }

        linkRepository.trackLink(chatId, link);
        return new SendMessage(chatId, ReplyConstants.TRACK_SUCCESS_TEXT);
    }
}
