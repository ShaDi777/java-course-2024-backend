package edu.java.services.apihandler;

import edu.java.client.dto.StackOverflowItem;
import edu.java.client.dto.StackOverflowResponse;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.dao.model.Link;
import edu.java.utils.LinkUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StackOverflowApiHandler implements ApiHandler {
    private static final String STACKOVERFLOW_DOMAIN_NAME = "stackoverflow.com";
    private final StackOverflowClient stackoverflowClient;
    private ApiHandler nextHandler = null;

    @Override
    public ApiHandlerResult handle(Link link) {
        if (link == null || link.getUrl() == null) {
            return ApiHandlerResult.getDefault();
        }

        if (supports(link)) {
            List<String> extensions = LinkUtils.getExtensions(link.getUrl());
            if (extensions.size() < 2) {
                return ApiHandlerResult.getDefault();
            }

            StackOverflowResponse response = stackoverflowClient.fetchQuestion(Long.valueOf(extensions.get(1)));
            StackOverflowItem responseItem = response.items()[0];
            if (link.getLastModified().isBefore(responseItem.lastModified())) {
                link.setLastModified(responseItem.lastModified());
                return ApiHandlerResult.getDefault()
                    .withHasUpdate(true)
                    .withDescription(generateDescription(responseItem));
            }
        }

        return (nextHandler == null) ? ApiHandlerResult.getDefault() : nextHandler.handle(link);
    }

    @Override
    public boolean supports(Link link) {
        return LinkUtils.getDomainName(link.getUrl()).equalsIgnoreCase(STACKOVERFLOW_DOMAIN_NAME);
    }

    @Override
    public void setNext(ApiHandler next) {
        this.nextHandler = next;
    }

    private String generateDescription(StackOverflowItem response) {
        return String.format("Проверьте \"%s\"", response.title());
    }
}
