package edu.java.client.stackoverflow;

import edu.java.client.dto.stackoverflow.StackOverflowCommentsResponse;
import edu.java.client.dto.stackoverflow.StackOverflowQuestionsResponse;
import org.jetbrains.annotations.NotNull;

public interface StackOverflowClient {
    StackOverflowQuestionsResponse fetchQuestion(@NotNull Long questionId);

    StackOverflowCommentsResponse fetchComments(@NotNull Long questionId);
}
