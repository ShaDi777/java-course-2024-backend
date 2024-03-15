package edu.java.bot.linktracker.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.linktracker.links.LinkRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListCommand implements Command {
    private final LinkRepository linkRepository;

    @Override
    public String command() {
        return CommandConstants.LIST_COMMAND;
    }

    @Override
    public String description() {
        return CommandConstants.LIST_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        List<String> links = linkRepository.getTrackedLinks(update.message().chat().id());

        var strBuilder = new StringBuilder(
            links.isEmpty()
                ? CommandConstants.LIST_EMPTY_REPLY_TEXT
                : CommandConstants.LIST_FILLED_REPLY_TEXT
        );

        for (var link : links) {
            strBuilder.append('\n');
            strBuilder.append(link);
        }

        return new SendMessage(update.message().chat().id(), strBuilder.toString()).disableWebPagePreview(true);
    }
}
