package edu.java.bot.dto;

import jakarta.validation.constraints.Min;
import java.net.URI;
import org.springframework.validation.annotation.Validated;

@Validated
public record LinkUpdateRequest(
    @Min(0)
    Long id,
    URI url,
    String description,
    Long[] tgChatsIds
) {
}
