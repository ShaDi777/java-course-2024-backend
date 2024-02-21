package edu.java.bot;

import edu.java.bot.dto.LinkUpdateRequest;
import edu.java.bot.dto.LinkUpdateResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class BotApiController {
    @PostMapping
    public LinkUpdateResponse createUpdate(@RequestBody LinkUpdateRequest request) {
        return new LinkUpdateResponse();
    }
}
