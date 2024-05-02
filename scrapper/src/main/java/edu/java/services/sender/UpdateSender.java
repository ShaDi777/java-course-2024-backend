package edu.java.services.sender;

import edu.java.dto.bot.BotLinkUpdateRequest;

public interface UpdateSender {
    void send(BotLinkUpdateRequest update);
}
