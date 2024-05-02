package edu.java.services.apihandler;

import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.dto.link.LinkInfoDto;
import edu.java.dto.link.StackOverflowLinkDto;
import edu.java.dto.stackoverflow.StackOverflowCommentsResponse;
import edu.java.dto.stackoverflow.StackOverflowItem;
import edu.java.dto.stackoverflow.StackOverflowResponse;
import edu.java.services.StackOverflowLinkService;
import edu.java.utils.LinkUtils;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StackOverflowApiHandler implements ApiHandler {
    public static final String QUESTION_STATUS_CHANGE_MESSAGE = "Изменился статус вопроса! Теперь ";
    public static final String NEW_ANSWER_MESSAGE = "Появился новый ответ!";
    public static final String NEW_COMMENT_MESSAGE = "Появился новый комментарий!";
    public static final String STATUS_ANSWERED = "отвеченный";
    public static final String STATUS_NOT_ANSWERED = "нет принятого ответа";

    private static final String STACKOVERFLOW_DOMAIN_NAME = "stackoverflow.com";

    private final StackOverflowClient stackoverflowClient;
    private final StackOverflowLinkService stackOverflowLinkService;
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

            long questionId = Long.parseLong(extensions.get(1));
            StackOverflowCommentsResponse commentsResponse = stackoverflowClient.fetchComments(questionId);
            StackOverflowResponse response = stackoverflowClient.fetchQuestion(questionId);
            StackOverflowItem responseItem = response.items()[0];

            Optional<StackOverflowLinkDto> previousLinkState = stackOverflowLinkService.findById(link.getLinkId());
            StackOverflowLinkDto currentLinkState = StackOverflowLinkDto.builder()
                .linkId(link.getLinkId())
                .commentsCount(commentsResponse.comments().length)
                .answersCount(responseItem.answersCount())
                .isAnswered(responseItem.isAnswered())
                .build();

            stackOverflowLinkService.upsertLink(currentLinkState);

            if (link.getLastModified().isBefore(responseItem.lastModified())) {
                link.setLastModified(responseItem.lastModified());
                return ApiHandlerResult.getDefault()
                    .withHasUpdate(true)
                    .withDescription(
                        generateDescription(
                            responseItem,
                            previousLinkState,
                            currentLinkState
                        )
                    );
            }
        }

        return (nextHandler == null) ? ApiHandlerResult.getDefault() : nextHandler.handle(link);
    }

    @Override
    public boolean supports(String linkUrl) {
        return LinkUtils.getDomainName(linkUrl).equalsIgnoreCase(STACKOVERFLOW_DOMAIN_NAME);
    }

    @Override
    public void setNext(ApiHandler next) {
        this.nextHandler = next;
    }

    private String generateDescription(
        StackOverflowItem basicResponse,
        Optional<StackOverflowLinkDto> previousState,
        StackOverflowLinkDto currentState
    ) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("Проверьте \"%s\"", basicResponse.title()));

        if (previousState.isEmpty()) {
            return stringBuilder.toString();
        }
        StackOverflowLinkDto previousStatePresented = previousState.get();

        if (previousStatePresented.getIsAnswered() != currentState.getIsAnswered()) {
            stringBuilder.append('\n');
            stringBuilder.append(QUESTION_STATUS_CHANGE_MESSAGE);
            stringBuilder.append(currentState.getIsAnswered() ? STATUS_ANSWERED : STATUS_NOT_ANSWERED);
            stringBuilder.append('.');
        }
        if (previousStatePresented.getAnswersCount() < currentState.getAnswersCount()) {
            stringBuilder.append('\n');
            stringBuilder.append(NEW_ANSWER_MESSAGE);
        }
        if (previousStatePresented.getCommentsCount() < currentState.getCommentsCount()) {
            stringBuilder.append('\n');
            stringBuilder.append(NEW_COMMENT_MESSAGE);
        }

        return stringBuilder.toString();
    }
}
