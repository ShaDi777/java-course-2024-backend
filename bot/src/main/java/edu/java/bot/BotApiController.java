package edu.java.bot;

import edu.java.bot.dto.LinkUpdateRequest;
import edu.java.bot.dto.LinkUpdateResponse;
import edu.java.bot.services.LinkUpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class BotApiController {
    private final LinkUpdateHandler linkUpdateHandler;

    @PostMapping
    public LinkUpdateResponse createUpdate(@RequestBody LinkUpdateRequest request) {
        linkUpdateHandler.handle(request.url().toString(), request.description(), request.tgChatsIds());
        return new LinkUpdateResponse();
    }
}
