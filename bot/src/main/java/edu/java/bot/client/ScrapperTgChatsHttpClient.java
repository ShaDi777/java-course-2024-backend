package edu.java.bot.client;

import edu.java.bot.client.dto.ScrapperTgChatResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperTgChatsHttpClient {
    private static final String BASE_URL = "http://localhost:8080";
    private static final String PATH_CHAT_BY_ID = "/tg-chat/{id}";
    private final WebClient webClient;

    public ScrapperTgChatsHttpClient() {
        this(BASE_URL);
    }

    public ScrapperTgChatsHttpClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public ScrapperTgChatResponse addTgChat(Long tgChatId) {
        return webClient
            .post()
            .uri(PATH_CHAT_BY_ID, tgChatId)
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, (response) -> Mono.empty())
            .onStatus(HttpStatusCode::is5xxServerError, (response) -> Mono.empty())
            .bodyToMono(ScrapperTgChatResponse.class)
            .block();
    }

    public ScrapperTgChatResponse deleteTgChat(Long tgChatId) {
        return webClient
            .delete()
            .uri(PATH_CHAT_BY_ID, tgChatId)
            .retrieve()
            .bodyToMono(ScrapperTgChatResponse.class)
            .block();
    }
}
