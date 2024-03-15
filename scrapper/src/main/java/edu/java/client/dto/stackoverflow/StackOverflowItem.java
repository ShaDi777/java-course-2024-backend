package edu.java.client.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowItem(
    @JsonProperty("title") String title,
    @JsonProperty("is_answered") Boolean isAnswered,
    @JsonProperty("answer_count") Integer answersCount,
    @JsonProperty("last_activity_date") OffsetDateTime lastModified
) {
}
