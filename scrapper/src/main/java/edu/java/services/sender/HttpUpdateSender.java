package edu.java.services.sender;

import edu.java.client.BotHttpClient;
import edu.java.dto.bot.BotLinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.use-queue", havingValue = "false", matchIfMissing = true)
@RequiredArgsConstructor
public class HttpUpdateSender implements UpdateSender {
    private final BotHttpClient botHttpClient;

    @Override
    public void send(BotLinkUpdateRequest update) {
        botHttpClient.update(
            update.id(),
            update.url(),
            update.description(),
            update.tgChatsIds()
        );
    }
}
