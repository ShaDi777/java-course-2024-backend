package edu.java.bot.client.dto;

import java.net.URI;
import org.springframework.validation.annotation.Validated;

@Validated
public record ScrapperLinkResponse(
    Long id,
    URI url
) {
}
