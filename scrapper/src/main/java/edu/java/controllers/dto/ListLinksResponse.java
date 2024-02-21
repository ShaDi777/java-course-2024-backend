package edu.java.controllers.dto;

public record ListLinksResponse(
    LinkResponse[] links,
    Integer size
) {
}
