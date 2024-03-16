package edu.java.dto.link;

public record ListLinksResponse(
    LinkResponse[] links,
    Integer size
) {
}
