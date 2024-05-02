package edu.java.scrapper.apihandler;

import edu.java.client.github.GitHubClient;
import edu.java.dto.github.GitHubResponse;
import edu.java.dto.link.LinkInfoDto;
import edu.java.services.apihandler.ApiHandler;
import edu.java.services.apihandler.ApiHandlerResult;
import edu.java.services.apihandler.GitHubApiHandler;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GitHubApiHandlerTest {
    private final GitHubClient client = Mockito.mock(GitHubClient.class);
    private final ApiHandler apiHandler = new GitHubApiHandler(client);

    @Test
    void defaultResultWhenUnsupportedLink() {
        String unsupportedDomainUrl = "https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array";
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
        when(client.fetchRepository(any(), any()))
            .thenReturn(new GitHubResponse("title", OffsetDateTime.MIN));

        String supportedLinkUrl = "https://github.com/ShaDi777/java-course-2024-backend";

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
        when(client.fetchRepository(any(), any()))
            .thenReturn(new GitHubResponse("title", OffsetDateTime.now()));

        String supportedLinkUrl = "https://github.com/ShaDi777/java-course-2024-backend";

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
}
