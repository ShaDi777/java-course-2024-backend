package edu.java.controllers.dto;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
