package edu.java.bot.linktracker.replies.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.linktracker.replies.Reply;
import java.util.List;

public interface UserReplyProcessor {
    List<? extends Reply> replies();

    SendMessage process(Update update);
}
