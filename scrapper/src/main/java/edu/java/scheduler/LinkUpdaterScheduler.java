package edu.java.scheduler;

import edu.java.dto.bot.BotLinkUpdateRequest;
import edu.java.dto.chat.TgChatInfoDto;
import edu.java.dto.link.LinkInfoDto;
import edu.java.services.LinkChatService;
import edu.java.services.LinkService;
import edu.java.services.LinkUpdater;
import edu.java.services.apihandler.ApiHandler;
import edu.java.services.apihandler.ApiHandlerResult;
import edu.java.services.sender.UpdateSender;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@EnableScheduling
@RequiredArgsConstructor
public class LinkUpdaterScheduler implements LinkUpdater {
    private final static int CHECK_PER_UPDATE = 10;
    private final UpdateSender updateSender;
    private final LinkService linkService;
    private final LinkChatService linkChatService;
    private final ApiHandler apiHandler;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public int update() {
        log.info("Updating...");

        List<LinkInfoDto> links = linkService.listByOldestCheck(CHECK_PER_UPDATE);
        int updatedCount = 0;
        for (var link : links) {
            ApiHandlerResult result = apiHandler.handle(link);
            if (result.hasUpdate()) {
                updatedCount++;

                BotLinkUpdateRequest update = new BotLinkUpdateRequest(
                    link.getLinkId(),
                    URI.create(link.getUrl()),
                    result.description(),
                    linkChatService.listAllChatsByLinkId(link.getLinkId())
                        .stream()
                        .map(TgChatInfoDto::getChatId)
                        .toArray(Long[]::new)
                );

                updateSender.send(update);

                linkService.updateLastModified(link.getLinkId(), link.getLastModified());
            }
            linkService.updateLastChecked(link.getLinkId(), OffsetDateTime.now());
        }

        return updatedCount;
    }
}
