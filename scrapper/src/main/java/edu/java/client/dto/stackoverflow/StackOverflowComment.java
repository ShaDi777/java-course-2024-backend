package edu.java.client.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowComment(
    @JsonProperty("creation_date") OffsetDateTime creationDate
) {
}
