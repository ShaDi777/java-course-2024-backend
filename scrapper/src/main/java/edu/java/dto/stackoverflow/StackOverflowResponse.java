package edu.java.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowResponse(
    @JsonProperty("items") StackOverflowItem[] items
) {
}
