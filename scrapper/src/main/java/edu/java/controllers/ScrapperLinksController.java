package edu.java.controllers;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.LinkResponse;
import edu.java.controllers.dto.ListLinksResponse;
import edu.java.controllers.dto.RemoveLinkRequest;
import edu.java.mapping.LinkMapper;
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
    private final LinkMapper linkMapper;
    private final LinkService linkService;

    @GetMapping
    public ListLinksResponse getTrackedLinks(
        @RequestHeader("Tg-Chat-Id") @Min(0) long tgChatId
    ) {
        var array = linkService.listAllByChatId(tgChatId)
            .stream()
            .map(linkMapper::linkToResponse)
            .toArray(LinkResponse[]::new);

        return new ListLinksResponse(array, array.length);
    }

    @PostMapping
    public LinkResponse addLink(
        @RequestHeader("Tg-Chat-Id") @Min(0) long tgChatId,
        @Validated @RequestBody AddLinkRequest request
    ) {
        return linkMapper.linkToResponse(
            linkService.add(tgChatId, request.link())
        );
    }

    @DeleteMapping
    public LinkResponse removeLink(
        @RequestHeader("Tg-Chat-Id") @Min(0) long tgChatId,
        @Validated @RequestBody RemoveLinkRequest request
    ) {
        return linkMapper.linkToResponse(
            linkService.remove(tgChatId, request.link())
        );
    }
}
