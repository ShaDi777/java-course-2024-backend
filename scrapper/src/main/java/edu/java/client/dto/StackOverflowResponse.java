package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowResponse(
    @JsonProperty("items") StackOverflowItem[] items
) {
}
