package edu.java.dto.link;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
