package edu.java.services;

import edu.java.dto.bot.BotLinkUpdateRequest;
import java.util.List;

public interface LinkUpdaterService {
    List<BotLinkUpdateRequest> update();
}
