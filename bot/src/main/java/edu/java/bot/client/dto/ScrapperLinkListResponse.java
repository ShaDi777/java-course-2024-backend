package edu.java.bot.client.dto;

import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

@Validated
public record ScrapperLinkListResponse(
    ScrapperLinkResponse[] links,
    @Min(0)
    Integer size
) {
}
