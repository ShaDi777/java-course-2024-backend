package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.StackOverflowCommentsResponse;
import edu.java.dto.stackoverflow.StackOverflowResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
public class StackOverflowWebClient implements StackOverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com/2.3/";
    private static final String SERVER_ERROR_LOG_WITH_STATUS_CODE = "StackOverflow server error. Status code: ";
    private static final String WEBCLIENT_RESPONSE_EXCEPTION_LOG = "WebclientResponseException: ";
    private static final String UNKNOWN_EXCEPTION_LOG = "Unknown exception occurred! ";

    private final WebClient webClient;
    private final Retry retry;

    public StackOverflowWebClient(Retry retry) {
        this(BASE_URL, retry);
    }

    public StackOverflowWebClient(String baseUrl, Retry retry) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.retry = retry;
    }

    @Override
    public StackOverflowResponse fetchQuestion(@NotNull Long questionId) {
        return this.webClient
            .get()
            .uri("/questions/{questionId}?site=stackoverflow", questionId)
            .retrieve()
            .onStatus(HttpStatus.NOT_FOUND::equals, (response) -> Mono.empty())
            .onStatus(HttpStatusCode::is5xxServerError, (response) -> {
                log.error(SERVER_ERROR_LOG_WITH_STATUS_CODE + response.statusCode());
                return response.createError();
            })
            .bodyToMono(StackOverflowResponse.class)
            .retryWhen(retry)
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
                return response.createError();
            })
            .bodyToMono(StackOverflowCommentsResponse.class)
            .retryWhen(retry)
            .block();
    }
}
