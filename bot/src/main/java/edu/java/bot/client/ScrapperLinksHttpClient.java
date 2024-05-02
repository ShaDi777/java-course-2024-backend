package edu.java.bot.client;

import edu.java.bot.client.dto.ScrapperLinkListResponse;
import edu.java.bot.client.dto.ScrapperLinkRequest;
import edu.java.bot.client.dto.ScrapperLinkResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class ScrapperLinksHttpClient {
    private static final String BASE_URL = "http://localhost:8080";
    private static final String PATH_LINKS = "/links";
    private static final String HEADER_TG_CHAT = "Tg-Chat-Id";
    private final WebClient webClient;
    private final Retry retry;

    public ScrapperLinksHttpClient(Retry retry) {
        this(BASE_URL, retry);
    }

    public ScrapperLinksHttpClient(String baseUrl, Retry retry) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.retry = retry;
    }

    public ScrapperLinkListResponse getLinks(Long tgChatId) {
        return webClient
            .get()
            .uri(PATH_LINKS)
            .header(HEADER_TG_CHAT, tgChatId.toString())
            .retrieve()
            .bodyToMono(ScrapperLinkListResponse.class)
            .retryWhen(retry)
            .block();
    }

    public ScrapperLinkResponse addLink(Long tgChatId, String link) {
        return webClient
            .post()
            .uri(PATH_LINKS)
            .header(HEADER_TG_CHAT, tgChatId.toString())
            .body(Mono.just(new ScrapperLinkRequest(link)), ScrapperLinkRequest.class)
            .retrieve()
            .bodyToMono(ScrapperLinkResponse.class)
            .retryWhen(retry)
            .block();
    }

    public ScrapperLinkResponse deleteLink(Long tgChatId, String link) {
        return webClient
            .method(HttpMethod.DELETE)
            .uri(PATH_LINKS)
            .header(HEADER_TG_CHAT, tgChatId.toString())
            .body(Mono.just(new ScrapperLinkRequest(link)), ScrapperLinkRequest.class)
            .retrieve()
            .bodyToMono(ScrapperLinkResponse.class)
            .retryWhen(retry)
            .block();
    }
}
