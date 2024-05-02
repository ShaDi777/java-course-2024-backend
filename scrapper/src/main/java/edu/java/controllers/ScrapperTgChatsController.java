package edu.java.controllers;

import edu.java.dto.chat.TgChatResponse;
import edu.java.services.TgChatService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class ScrapperTgChatsController {
    private static final String ID = "id";
    private static final String PATH_ID = "/{id}";

    private final TgChatService tgChatService;

    @PostMapping(PATH_ID)
    public TgChatResponse addChat(@PathVariable(ID) @Min(0) long tgChatId) {
        tgChatService.register(tgChatId);
        return new TgChatResponse();
    }

    @DeleteMapping(PATH_ID)
    public TgChatResponse deleteChat(@PathVariable(ID) @Min(0) long tgChatId) {
        tgChatService.unregister(tgChatId);
        return new TgChatResponse();
    }
}
