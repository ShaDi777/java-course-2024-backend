package edu.java.bot.linktracker.commands.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.linktracker.commands.Command;
import java.util.List;

public interface UserCommandProcessor {
    List<? extends Command> commands();

    SendMessage process(Update update);
}
