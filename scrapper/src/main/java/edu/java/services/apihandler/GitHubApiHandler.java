package edu.java.services.apihandler;

import edu.java.client.github.GitHubClient;
import edu.java.dto.github.GitHubResponse;
import edu.java.dto.link.LinkInfoDto;
import edu.java.utils.LinkUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GitHubApiHandler implements ApiHandler {
    private static final String GITHUB_DOMAIN_NAME = "github.com";
    private final GitHubClient githubClient;
    private ApiHandler nextHandler = null;

    @Override
    public ApiHandlerResult handle(LinkInfoDto link) {
        if (link == null || link.getUrl() == null) {
            return ApiHandlerResult.getDefault();
        }

        if (supports(link.getUrl())) {
            List<String> extensions = LinkUtils.getExtensions(link.getUrl());
            if (extensions.size() < 2) {
                return ApiHandlerResult.getDefault();
            }

            GitHubResponse response = githubClient.fetchRepository(extensions.get(0), extensions.get(1));
            if (link.getLastModified().isBefore(response.lastModified())) {
                link.setLastModified(response.lastModified());
                return ApiHandlerResult.getDefault().withHasUpdate(true).withDescription(generateDescription(response));
            }
        }

        return (nextHandler == null) ? ApiHandlerResult.getDefault() : nextHandler.handle(link);
    }

    @Override
    public boolean supports(String linkUrl) {
        return LinkUtils.getDomainName(linkUrl).equalsIgnoreCase(GITHUB_DOMAIN_NAME);
    }

    @Override
    public void setNext(ApiHandler next) {
        this.nextHandler = next;
    }

    private String generateDescription(GitHubResponse response) {
        return String.format("Проверьте \"%s\"", response.title());
    }
}
