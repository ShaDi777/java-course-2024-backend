package edu.java.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowItem(
    @JsonProperty("title") String title,
    @JsonProperty("last_activity_date") OffsetDateTime lastModified
) {
}
