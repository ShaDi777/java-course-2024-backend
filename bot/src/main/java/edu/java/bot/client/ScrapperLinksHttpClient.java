package edu.java.bot.client;

import edu.java.bot.client.dto.ScrapperLinkListResponse;
import edu.java.bot.client.dto.ScrapperLinkRequest;
import edu.java.bot.client.dto.ScrapperLinkResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperLinksHttpClient {
    private static final String BASE_URL = "http://localhost:8080";
    private static final String PATH_LINKS = "/links";
    private static final String HEADER_TG_CHAT = "Tg-Chat-Id";
    private final WebClient webClient;

    public ScrapperLinksHttpClient() {
        this(BASE_URL);
    }

    public ScrapperLinksHttpClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public ScrapperLinkListResponse getLinks(Long tgChatId) {
        return webClient
            .get()
            .uri(PATH_LINKS)
            .header(HEADER_TG_CHAT, tgChatId.toString())
            .retrieve()
            .bodyToMono(ScrapperLinkListResponse.class)
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
            .block();
    }
}
