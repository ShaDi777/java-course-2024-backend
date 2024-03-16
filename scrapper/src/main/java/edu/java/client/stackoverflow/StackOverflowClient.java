package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.StackOverflowResponse;
import org.jetbrains.annotations.NotNull;

public interface StackOverflowClient {
    StackOverflowResponse fetchQuestion(@NotNull Long questionId);
}
