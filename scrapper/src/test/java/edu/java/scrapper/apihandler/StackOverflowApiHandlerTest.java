package edu.java.scrapper.apihandler;

import edu.java.client.dto.StackOverflowItem;
import edu.java.client.dto.StackOverflowResponse;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.dao.model.Link;
import edu.java.services.apihandler.ApiHandler;
import edu.java.services.apihandler.ApiHandlerResult;
import edu.java.services.apihandler.StackOverflowApiHandler;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StackOverflowApiHandlerTest {
    private final StackOverflowClient client = Mockito.mock(StackOverflowClient.class);
    private final ApiHandler apiHandler = new StackOverflowApiHandler(client);

    @Test
    void defaultResultWhenUnsupportedLink() {
        String unsupportedDomainUrl = "https://github.com/ShaDi777/java-course-2024-backend";
        Link link = Link.builder()
            .linkId(1L)
            .url(unsupportedDomainUrl)
            .lastChecked(OffsetDateTime.now())
            .lastModified(OffsetDateTime.now())
            .build();

        ApiHandlerResult result = apiHandler.handle(link);

        assertThat(result).isNotNull().isEqualTo(ApiHandlerResult.getDefault());
        assertThat(result.hasUpdate()).isFalse();
    }

    @Test
    void noUpdatesResult() {
        when(client.fetchQuestion(any()))
            .thenReturn(
                new StackOverflowResponse(new StackOverflowItem[] {
                    new StackOverflowItem("title", OffsetDateTime.MIN)
                })
            );

        String supportedLinkUrl = "https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array";

        Link link = Link.builder()
            .linkId(1L)
            .url(supportedLinkUrl)
            .lastChecked(OffsetDateTime.now())
            .lastModified(OffsetDateTime.MIN)
            .build();

        ApiHandlerResult result = apiHandler.handle(link);

        assertThat(result).isNotNull().isEqualTo(ApiHandlerResult.getDefault());
        assertThat(result.hasUpdate()).isFalse();
    }

    @Test
    void hasUpdateResult() {
        when(client.fetchQuestion(any()))
            .thenReturn(
                new StackOverflowResponse(new StackOverflowItem[] {
                    new StackOverflowItem("title", OffsetDateTime.now())
                })
            );

        String supportedLinkUrl = "\"https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array\"";

        Link link = Link.builder()
            .linkId(1L)
            .url(supportedLinkUrl)
            .lastChecked(OffsetDateTime.now())
            .lastModified(OffsetDateTime.MIN)
            .build();

        ApiHandlerResult result = apiHandler.handle(link);

        assertThat(result).isNotNull();
        assertThat(result.hasUpdate()).isTrue();
    }
}

