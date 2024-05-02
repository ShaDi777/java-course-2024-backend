package edu.java.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubResponse(
    @JsonProperty("name") String title,
    @JsonProperty("updated_at") OffsetDateTime lastModified
) {
}
