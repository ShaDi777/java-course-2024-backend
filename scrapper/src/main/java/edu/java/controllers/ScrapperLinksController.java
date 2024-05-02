package edu.java.controllers;

import edu.java.dto.link.AddLinkRequest;
import edu.java.dto.link.LinkResponse;
import edu.java.dto.link.ListLinksResponse;
import edu.java.dto.link.RemoveLinkRequest;
import edu.java.mapping.LinkMapper;
import edu.java.services.LinkChatService;
import edu.java.services.LinkService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class ScrapperLinksController {
    private static final String TG_HEADER = "Tg-Chat-Id";

    private final LinkMapper linkMapper;
    private final LinkService linkService;
    private final LinkChatService linkChatService;

    @GetMapping
    public ListLinksResponse getTrackedLinks(
        @RequestHeader(TG_HEADER) @Min(0) long tgChatId
    ) {
        var array = linkChatService.listAllLinksByChatId(tgChatId)
            .stream()
            .map(linkMapper::linkInfoDtoToResponse)
            .toArray(LinkResponse[]::new);

        return new ListLinksResponse(array, array.length);
    }

    @PostMapping
    public LinkResponse addLink(
        @RequestHeader(TG_HEADER) @Min(0) long tgChatId,
        @Validated @RequestBody AddLinkRequest request
    ) {
        return linkMapper.linkInfoDtoToResponse(
            linkService.add(tgChatId, request.link())
        );
    }

    @DeleteMapping
    public LinkResponse removeLink(
        @RequestHeader(TG_HEADER) @Min(0) long tgChatId,
        @Validated @RequestBody RemoveLinkRequest request
    ) {
        return linkMapper.linkInfoDtoToResponse(
            linkService.remove(tgChatId, request.link())
        );
    }
}
