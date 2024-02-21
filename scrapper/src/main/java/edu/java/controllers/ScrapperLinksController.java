package edu.java.controllers;

import edu.java.controllers.dto.AddLinkRequest;
import edu.java.controllers.dto.LinkResponse;
import edu.java.controllers.dto.ListLinksResponse;
import edu.java.controllers.dto.RemoveLinkRequest;
import jakarta.validation.constraints.Min;
import java.net.URI;
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
public class ScrapperLinksController {
    @GetMapping
    public ListLinksResponse getTrackedLinks(
        @RequestHeader("Tg-Chat-Id") @Min(0) long tgChatId
    ) {
        LinkResponse[] array = {null};
        return new ListLinksResponse(array, array.length);
    }

    @PostMapping
    public LinkResponse addLink(
        @RequestHeader("Tg-Chat-Id") @Min(0) long tgChatId,
        @Validated @RequestBody AddLinkRequest request
    ) {
        return new LinkResponse(tgChatId, URI.create(request.link()));
    }

    @DeleteMapping
    public LinkResponse removeLink(
        @RequestHeader("Tg-Chat-Id") @Min(0) long tgChatId,
        @Validated @RequestBody RemoveLinkRequest request
    ) {
        return new LinkResponse(tgChatId, URI.create(request.link()));
    }
}
