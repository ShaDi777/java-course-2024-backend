package edu.java.controllers;

import edu.java.controllers.dto.TgChatResponse;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class ScrapperTgChatsController {
    @PostMapping("/{id}")
    public TgChatResponse addChat(@PathVariable("id") @Min(0) long tgChatId) {
        return new TgChatResponse();
    }

    @DeleteMapping("/{id}")
    public TgChatResponse deleteChat(@PathVariable("id") @Min(0) long tgChatId) {
        return new TgChatResponse();
    }
}
