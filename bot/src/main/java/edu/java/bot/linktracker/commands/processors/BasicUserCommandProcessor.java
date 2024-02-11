package edu.java.bot.linktracker.commands.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.linktracker.commands.Command;
import edu.java.bot.linktracker.commands.CommandConstants;
import edu.java.bot.linktracker.commands.HelpCommand;
import edu.java.bot.linktracker.commands.ListCommand;
import edu.java.bot.linktracker.commands.StartCommand;
import edu.java.bot.linktracker.commands.TrackCommand;
import edu.java.bot.linktracker.commands.UntrackCommand;
import edu.java.bot.linktracker.links.LinkRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("BasicUserCommandProcessor")
public class BasicUserCommandProcessor implements UserCommandProcessor {
    private final List<? extends Command> commands;

    public BasicUserCommandProcessor(@Autowired LinkRepository linkRepository) {
        var emptyHelpCommand = new HelpCommand();
        var cmds = new ArrayList<>(
            List.of(
                emptyHelpCommand,
                new StartCommand(),
                new TrackCommand(),
                new UntrackCommand(),
                new ListCommand(linkRepository)
            )
        );
        cmds.add(new HelpCommand(cmds.toArray(Command[]::new)));
        cmds.remove(emptyHelpCommand);
        commands = cmds;
    }

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        for (var command : commands) {
            if (command.supports(update)) {
                return command.handle(update);
            }
        }

        return new SendMessage(update.message().chat().id(), CommandConstants.UNSUPPORTED_TEXT);
    }
}
