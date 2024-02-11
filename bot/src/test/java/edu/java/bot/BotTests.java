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
import edu.java.bot.linktracker.commands.processors.BasicUserCommandProcessor;
import edu.java.bot.linktracker.links.LinkRepository;
import edu.java.bot.linktracker.replies.ReplyConstants;
import edu.java.bot.linktracker.replies.processors.BasicUserReplyProcessor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BotTests {
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
        var messageProcessor = new BasicUserCommandProcessor(linksRepository);
        var replyProcessor = new BasicUserReplyProcessor(linksRepository);
        linkTrackerBot = new LinkTrackerBot("fakeToken", replyProcessor, messageProcessor);

        ReflectionTestUtils.setField(linkTrackerBot, "bot", bot);
    }

    @Test
    public void unknownCommandTest() {
        // given
        String unknownCommand = "/unknownCommand";

        // when
        String answer = executeCommand(unknownCommand, false, null);

        // then
        assertThat(fakeUpdate.message().text()).isEqualTo(unknownCommand);
        assertThat(answer).isEqualTo(CommandConstants.UNSUPPORTED_TEXT);
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

    @Test
    public void trackCommand() {
        // given
        String command = CommandConstants.TRACK_COMMAND;

        // when
        String answer = executeCommand(command, false, null);

        // then
        assertThat(fakeUpdate.message().text()).isEqualTo(command);
        assertThat(answer).isEqualTo(CommandConstants.TRACK_REPLY_TEXT);
    }

    @Test
    public void untrackCommand() {
        // given
        String command = CommandConstants.UNTRACK_COMMAND;

        // when
        String answer = executeCommand(command, false, null);

        // then
        assertThat(fakeUpdate.message().text()).isEqualTo(command);
        assertThat(answer).isEqualTo(CommandConstants.UNTRACK_REPLY_TEXT);
    }

    @Test
    public void replySuccessTrack() {
        // given
        String link = "https://github.com";
        String replyOnText = CommandConstants.TRACK_REPLY_TEXT;
        when(linksRepository.isValidLink(link)).thenReturn(true);

        // when
        String answer = executeCommand(link, true, replyOnText);

        Mockito.verify(linksRepository, times(1)).trackLink(chatId, link);
        assertThat(fakeUpdate.message().text()).isEqualTo(link);
        assertThat(answer).isEqualTo(ReplyConstants.TRACK_SUCCESS_TEXT);
    }

    @Test
    public void replySuccessUntrack() {
        // given
        String link = "https://github.com";
        String replyOnText = CommandConstants.UNTRACK_REPLY_TEXT;
        when(linksRepository.isValidLink(link)).thenReturn(true);

        // when
        String answer = executeCommand(link, true, replyOnText);

        Mockito.verify(linksRepository, times(1)).untrackLink(chatId, link);
        assertThat(fakeUpdate.message().text()).isEqualTo(link);
        assertThat(answer).isEqualTo(ReplyConstants.UNTRACK_SUCCESS_TEXT);
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
