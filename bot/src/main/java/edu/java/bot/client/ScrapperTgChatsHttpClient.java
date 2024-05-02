package edu.java.bot.client;

import edu.java.bot.client.dto.ScrapperTgChatResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class ScrapperTgChatsHttpClient {
    private static final String BASE_URL = "http://localhost:8080";
    private static final String PATH_CHAT_BY_ID = "/tg-chat/{id}";
    private final WebClient webClient;
    private final Retry retry;

    public ScrapperTgChatsHttpClient(Retry retry) {
        this(BASE_URL, retry);
    }

    public ScrapperTgChatsHttpClient(String baseUrl, Retry retry) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.retry = retry;
    }

    public ScrapperTgChatResponse addTgChat(Long tgChatId) {
        return webClient
            .post()
            .uri(PATH_CHAT_BY_ID, tgChatId)
            .retrieve()
            .onStatus(HttpStatus.BAD_REQUEST::equals, ClientResponse::createError)
            .onStatus(HttpStatusCode::is5xxServerError, ClientResponse::createError)
            .bodyToMono(ScrapperTgChatResponse.class)
            .retryWhen(retry)
            .onErrorResume(WebClientResponseException.class, exception -> Mono.empty())
            .block();
    }

    public ScrapperTgChatResponse deleteTgChat(Long tgChatId) {
        return webClient
            .delete()
            .uri(PATH_CHAT_BY_ID, tgChatId)
            .retrieve()
            .bodyToMono(ScrapperTgChatResponse.class)
            .retryWhen(retry)
            .onErrorResume(WebClientResponseException.class, exception -> Mono.empty())
            .block();
    }
}
