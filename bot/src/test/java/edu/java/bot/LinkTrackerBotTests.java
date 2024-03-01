package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.linktracker.bot.LinkTrackerBot;
import edu.java.bot.linktracker.commands.CommandConstants;
import edu.java.bot.linktracker.commands.HelpCommand;
import edu.java.bot.linktracker.commands.ListCommand;
import edu.java.bot.linktracker.commands.StartCommand;
import edu.java.bot.linktracker.commands.TrackCommand;
import edu.java.bot.linktracker.commands.UntrackCommand;
import edu.java.bot.linktracker.commands.processors.BasicUserCommandProcessor;
import edu.java.bot.linktracker.links.LinkRepository;
import edu.java.bot.linktracker.replies.ReplyConstants;
import edu.java.bot.linktracker.replies.TrackReply;
import edu.java.bot.linktracker.replies.UntrackReply;
import edu.java.bot.linktracker.replies.processors.BasicUserReplyProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinkTrackerBotTests {
    private final long chatId = 1L;
    Update fakeUpdate = Mockito.mock(Update.class);
    LinkTrackerBot linkTrackerBot;
    LinkRepository linksRepository;

    @Mock
    TelegramBot bot;

    @Captor
    ArgumentCaptor<? extends BaseRequest<SendMessage, SendResponse>> sendMessageCaptor;

    @BeforeEach
    public void InitBot() {
        linksRepository = Mockito.mock(LinkRepository.class);
        var commands = new ArrayList<>(List.of(
            new ListCommand(linksRepository),
            new StartCommand(),
            new TrackCommand(),
            new UntrackCommand()
        ));
        commands.add(new HelpCommand(commands));

        var messageProcessor = new BasicUserCommandProcessor(commands);
        var replyProcessor = new BasicUserReplyProcessor(List.of(
                new TrackReply(linksRepository),
                new UntrackReply(linksRepository)
        ));
        linkTrackerBot = new LinkTrackerBot(bot, replyProcessor, messageProcessor);
    }

    private static Stream<Arguments> parametersBasicCommand() {
        return Stream.of(
            Arguments.of("/unknownCommand", CommandConstants.UNSUPPORTED_TEXT),
            Arguments.of( CommandConstants.TRACK_COMMAND, CommandConstants.TRACK_REPLY_TEXT),
            Arguments.of( CommandConstants.UNTRACK_COMMAND, CommandConstants.UNTRACK_REPLY_TEXT)
        );
    }

    @ParameterizedTest
    @MethodSource("parametersBasicCommand")
    public void basicCommandTest(String command, String expectedAnswer) {
        String answer = executeCommand(command, false, null);

        assertThat(fakeUpdate.message().text()).isEqualTo(command);
        assertThat(answer).isEqualTo(expectedAnswer);
    }

    private static Stream<Arguments> parametersReplyCommand() {
        return Stream.of(
            Arguments.of(
                "https://github.com/ShaDi777/java-course-2024-backend",
                CommandConstants.TRACK_REPLY_TEXT,
                true,
                ReplyConstants.TRACK_SUCCESS_TEXT
            ),
            Arguments.of(
                "https://github.com/ShaDi777/java-course-2024-backend",
                CommandConstants.UNTRACK_REPLY_TEXT,
                true,
                ReplyConstants.UNTRACK_SUCCESS_TEXT
            ),
            Arguments.of(
                "NOT_A_LINK",
                CommandConstants.TRACK_REPLY_TEXT,
                false,
                ReplyConstants.TRACK_FAILURE_TEXT
            ),
            Arguments.of(
                "NOT_A_LINK",
                CommandConstants.UNTRACK_REPLY_TEXT,
                false,
                ReplyConstants.UNTRACK_FAILURE_TEXT
            )
        );
    }

    @ParameterizedTest
    @MethodSource("parametersReplyCommand")
    public void replyCommandTest(String link, String replyOnText, boolean isSuccess, String expectedAnswer) {
        when(linksRepository.isValidLink(link)).thenReturn(isSuccess);

        String answer = executeCommand(link, true, replyOnText);

        assertThat(fakeUpdate.message().text()).isEqualTo(link);
        assertThat(answer).isEqualTo(expectedAnswer);
    }

    @Test
    public void emptyListTracked() {
        // given
        when(linksRepository.getTrackedLinks(chatId)).thenReturn(List.of());
        String command = CommandConstants.LIST_COMMAND;

        // when
        String answer = executeCommand(command, false, null);

        // then
        assertThat(fakeUpdate.message().text()).isEqualTo(command);
        assertThat(answer).isEqualTo(CommandConstants.LIST_EMPTY_REPLY_TEXT);
    }

    @Test
    public void listTracked() {
        // given
        String link1 = "https://github.com";
        String link2 = "https://stackoverflow.com";
        String link3 = "https://stackoverflow.com/questions";
        when(linksRepository.getTrackedLinks(chatId)).thenReturn(List.of(link1, link2, link3));

        String command = CommandConstants.LIST_COMMAND;

        // when
        String answer = executeCommand(command, false, null);

        // then
        Mockito.verify(linksRepository, times(1)).getTrackedLinks(chatId);
        assertThat(fakeUpdate.message().text()).isEqualTo(command);
        assertThat(answer).startsWith(CommandConstants.LIST_FILLED_REPLY_TEXT);
        assertThat(answer).contains(link1).contains(link2).contains(link3);
    }

    @Test
    public void helpCommand() {
        // given
        String command = CommandConstants.HELP_COMMAND;

        // when
        String answer = executeCommand(command, false, null);

        // then
        assertThat(fakeUpdate.message().text()).isEqualTo(command);
        assertThat(answer)
            .contains(CommandConstants.START_COMMAND).contains(CommandConstants.START_DESCRIPTION)
            .contains(CommandConstants.HELP_COMMAND).contains(CommandConstants.HELP_DESCRIPTION)
            .contains(CommandConstants.LIST_COMMAND).contains(CommandConstants.LIST_DESCRIPTION)
            .contains(CommandConstants.TRACK_COMMAND).contains(CommandConstants.TRACK_DESCRIPTION)
            .contains(CommandConstants.UNTRACK_COMMAND).contains(CommandConstants.UNTRACK_DESCRIPTION);
    }

    // Help methods
    private void setTextForUpdate(String text) {
        var message = Mockito.mock(Message.class);
        var chat = Mockito.mock(Chat.class);
        when(fakeUpdate.message()).thenReturn(message);
        when(fakeUpdate.message().chat()).thenReturn(chat);
        when(fakeUpdate.message().chat().id()).thenReturn(chatId);
        when(fakeUpdate.message().text()).thenReturn(text);
    }

    private void setReplyMessage(String replyOnText) {
        var message = Mockito.mock(Message.class);
        when(message.text()).thenReturn(replyOnText);
        when(fakeUpdate.message().replyToMessage()).thenReturn(message);
    }

    private String executeCommand(String messageText, boolean isReply, String replyOnText) {
        setTextForUpdate(messageText);
        if (isReply) {
            setReplyMessage(replyOnText);
        }

        linkTrackerBot.process(List.of(fakeUpdate));

        Mockito.verify(bot).execute(sendMessageCaptor.capture());

        BaseRequest<SendMessage, SendResponse> value = sendMessageCaptor.getValue();
        return (String) value.getParameters().get("text");
    }
}
