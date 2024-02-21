package edu.java.client;

import edu.java.client.dto.bot.BotLinkUpdateRequest;
import edu.java.client.dto.bot.BotLinkUpdateResponse;
import java.net.URI;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotHttpClient {
    private static final String BASE_URL = "http:///localhost:8090";
    private static final String PATH_UPDATES = "/updates";
    private final WebClient webClient;

    public BotHttpClient() {
        this(BASE_URL);
    }

    public BotHttpClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public BotLinkUpdateResponse update(long linkId, URI url, String description, Long[] tgChatIds) {
        return webClient
            .post()
            .uri(PATH_UPDATES)
            .body(
                Mono.just(new BotLinkUpdateRequest(linkId, url, description, tgChatIds)),
                BotLinkUpdateRequest.class
            )
            .retrieve()
            .bodyToMono(BotLinkUpdateResponse.class)
            .block();
    }
}
