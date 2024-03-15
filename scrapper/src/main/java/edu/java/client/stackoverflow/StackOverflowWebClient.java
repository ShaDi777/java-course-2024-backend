package edu.java.client.stackoverflow;

import edu.java.client.dto.stackoverflow.StackOverflowCommentsResponse;
import edu.java.client.dto.stackoverflow.StackOverflowQuestionsResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
public class StackOverflowWebClient implements StackOverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com/2.3/";
    private static final String SERVER_ERROR_LOG_WITH_STATUS_CODE = "StackOverflow server error. Status code: ";
    private static final String WEBCLIENT_RESPONSE_EXCEPTION_LOG = "WebclientResponseException: ";
    private static final String UNKNOWN_EXCEPTION_LOG = "Unknown exception occurred! ";
    private final WebClient webClient;

    public StackOverflowWebClient() {
        this(BASE_URL);
    }

    public StackOverflowWebClient(String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public StackOverflowQuestionsResponse fetchQuestion(@NotNull Long questionId) {
        return this.webClient
            .get()
            .uri("/questions/{questionId}?site=stackoverflow", questionId)
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, (response) -> Mono.empty())
            .onStatus(HttpStatusCode::is5xxServerError, (response) -> {
                log.error(SERVER_ERROR_LOG_WITH_STATUS_CODE + response.statusCode());
                return Mono.empty();
            })
            .bodyToMono(StackOverflowQuestionsResponse.class)
            .onErrorMap(WebClientResponseException.class, (throwable) -> {
                log.error(WEBCLIENT_RESPONSE_EXCEPTION_LOG + throwable.getResponseBodyAsString());
                return null;
            })
            .onErrorMap(Exception.class, (throwable) -> {
                log.error(UNKNOWN_EXCEPTION_LOG + throwable.getMessage());
                return null;
            })
            .block();
    }

    @Override
    public StackOverflowCommentsResponse fetchComments(@NotNull Long questionId) {
        return this.webClient
            .get()
            .uri("/questions/{questionId}/comments?site=stackoverflow", questionId)
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, (response) -> Mono.empty())
            .onStatus(HttpStatusCode::is5xxServerError, (response) -> {
                log.error(SERVER_ERROR_LOG_WITH_STATUS_CODE + response.statusCode());
                return Mono.empty();
            })
            .bodyToMono(StackOverflowCommentsResponse.class)
            .onErrorMap(WebClientResponseException.class, (throwable) -> {
                log.error(WEBCLIENT_RESPONSE_EXCEPTION_LOG + throwable.getResponseBodyAsString());
                return null;
            })
            .onErrorMap(Exception.class, (throwable) -> {
                log.error(UNKNOWN_EXCEPTION_LOG + throwable.getMessage());
                return null;
            })
            .block();
    }
}
