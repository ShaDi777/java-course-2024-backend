package edu.java.scrapper.apihandler;

import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.dto.link.LinkInfoDto;
import edu.java.dto.link.StackOverflowLinkDto;
import edu.java.dto.stackoverflow.StackOverflowComment;
import edu.java.dto.stackoverflow.StackOverflowCommentsResponse;
import edu.java.dto.stackoverflow.StackOverflowItem;
import edu.java.dto.stackoverflow.StackOverflowResponse;
import edu.java.services.StackOverflowLinkService;
import edu.java.services.apihandler.ApiHandler;
import edu.java.services.apihandler.ApiHandlerResult;
import edu.java.services.apihandler.StackOverflowApiHandler;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StackOverflowApiHandlerTest {
    private final StackOverflowClient client = Mockito.mock(StackOverflowClient.class);
    private final StackOverflowLinkService stackOverflowLinkService = Mockito.mock(StackOverflowLinkService.class);
    private final ApiHandler apiHandler = new StackOverflowApiHandler(client, stackOverflowLinkService);

    @Test
    void defaultResultWhenUnsupportedLink() {
        String unsupportedDomainUrl = "https://github.com/ShaDi777/java-course-2024-backend";
        LinkInfoDto link = LinkInfoDto.builder()
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
                    new StackOverflowItem("title", true, 1, OffsetDateTime.MIN)
                })
            );
        when(client.fetchComments(any()))
            .thenReturn(
                new StackOverflowCommentsResponse(new StackOverflowComment[] {
                    new StackOverflowComment(OffsetDateTime.now())
                })
            );

        String supportedLinkUrl = "https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array";

        LinkInfoDto link = LinkInfoDto.builder()
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
                    new StackOverflowItem("title", true, 1, OffsetDateTime.now())
                })
            );
        when(client.fetchComments(any()))
            .thenReturn(
                new StackOverflowCommentsResponse(new StackOverflowComment[] {
                    new StackOverflowComment(OffsetDateTime.now())
                })
            );

        String supportedLinkUrl = "\"https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array\"";

        LinkInfoDto link = LinkInfoDto.builder()
            .linkId(1L)
            .url(supportedLinkUrl)
            .lastChecked(OffsetDateTime.now())
            .lastModified(OffsetDateTime.MIN)
            .build();

        ApiHandlerResult result = apiHandler.handle(link);

        assertThat(result).isNotNull();
        assertThat(result.hasUpdate()).isTrue();
    }

    @Test
    void hasUpdateWithNewAnswersComments() {
        long questionId = 11227809;
        long linkId = 1L;
        when(stackOverflowLinkService.findById(linkId)).thenReturn(
            Optional.of(
                StackOverflowLinkDto.builder()
                    .linkId(1L)
                    .isAnswered(false)
                    .answersCount(0)
                    .commentsCount(0)
                    .build()
            )
        );
        when(client.fetchQuestion(questionId))
            .thenReturn(
                new StackOverflowResponse(new StackOverflowItem[] {
                    new StackOverflowItem("title", true, 1, OffsetDateTime.now())
                })
            );
        when(client.fetchComments(questionId))
            .thenReturn(
                new StackOverflowCommentsResponse(new StackOverflowComment[] {
                    new StackOverflowComment(OffsetDateTime.now())
                })
            );

        String supportedLinkUrl = "\"https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array\"";

        LinkInfoDto link = LinkInfoDto.builder()
            .linkId(linkId)
            .url(supportedLinkUrl)
            .lastChecked(OffsetDateTime.now())
            .lastModified(OffsetDateTime.MIN)
            .build();

        ApiHandlerResult result = apiHandler.handle(link);

        assertThat(result).isNotNull();
        assertThat(result.hasUpdate()).isTrue();
        assertThat(result.description())
            .contains(StackOverflowApiHandler.NEW_ANSWER_MESSAGE)
            .contains(StackOverflowApiHandler.NEW_COMMENT_MESSAGE)
            .contains(StackOverflowApiHandler.QUESTION_STATUS_CHANGE_MESSAGE + StackOverflowApiHandler.STATUS_ANSWERED);
    }
}

