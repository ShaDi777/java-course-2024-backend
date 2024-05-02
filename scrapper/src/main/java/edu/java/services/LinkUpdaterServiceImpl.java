package edu.java.services;

import edu.java.dto.bot.BotLinkUpdateRequest;
import edu.java.dto.chat.TgChatInfoDto;
import edu.java.dto.link.LinkInfoDto;
import edu.java.services.apihandler.ApiHandler;
import edu.java.services.apihandler.ApiHandlerResult;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LinkUpdaterServiceImpl implements LinkUpdaterService {
    private final static int CHECK_PER_UPDATE = 10;
    private final LinkService linkService;
    private final LinkChatService linkChatService;
    private final ApiHandler apiHandler;

    @Transactional
    @Override
    public List<BotLinkUpdateRequest> update() {
        List<BotLinkUpdateRequest> linksUpdated = new ArrayList<>();

        List<LinkInfoDto> links = linkService.listByOldestCheck(CHECK_PER_UPDATE);
        for (var link : links) {
            ApiHandlerResult result = apiHandler.handle(link);
            if (result.hasUpdate()) {

                linksUpdated.add(
                    new BotLinkUpdateRequest(
                        link.getLinkId(),
                        URI.create(link.getUrl()),
                        result.description(),
                        linkChatService.listAllChatsByLinkId(link.getLinkId())
                                       .stream()
                                       .map(TgChatInfoDto::getChatId)
                                       .toArray(Long[]::new)
                    )
                );

                linkService.updateLastModified(link.getLinkId(), link.getLastModified());
            }
            linkService.updateLastChecked(link.getLinkId(), OffsetDateTime.now());
        }

        return linksUpdated;
    }
}
