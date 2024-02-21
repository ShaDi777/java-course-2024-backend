package edu.java.client.dto.bot;

import jakarta.validation.constraints.Min;
import java.net.URI;
import org.springframework.validation.annotation.Validated;

@Validated
public record BotLinkUpdateRequest(
    @Min(0)
    Long id,
    URI url,
    String description,
    Long[] tgChatsIds
) {
}
