package edu.java.client.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowQuestionsResponse(
    @JsonProperty("items") StackOverflowItem[] items
) {
}
