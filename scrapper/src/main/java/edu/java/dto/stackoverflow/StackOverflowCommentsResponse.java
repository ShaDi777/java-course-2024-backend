package edu.java.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowCommentsResponse(
    @JsonProperty("items") StackOverflowComment[] comments
) {
}
