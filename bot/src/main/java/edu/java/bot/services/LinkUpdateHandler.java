package edu.java.bot.services;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.linktracker.bot.Bot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkUpdateHandler {
    private final Bot bot;

    public void handle(String url, String description, Long[] tgChatIds) {
        String notificationMessage = getNotificationMessage(url, description);

        for (long chatId : tgChatIds) {
            SendMessage sendMessage = new SendMessage(chatId, notificationMessage)
                .disableWebPagePreview(true);

            bot.execute(sendMessage);
        }
    }

    private static String getNotificationMessage(String url, String description) {
        return "Появилось обновление для ссылки:\n"
            + url
            + '\n'
            + description;
    }
}
