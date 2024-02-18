package edu.java.client.stackoverflow;

import edu.java.client.dto.StackOverflowResponse;
import org.jetbrains.annotations.NotNull;

public interface StackOverflowClient {
    StackOverflowResponse fetchQuestion(@NotNull Long questionId);
}
