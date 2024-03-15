package edu.java.scheduler;

import edu.java.client.BotHttpClient;
import edu.java.dao.model.Link;
import edu.java.dao.model.TgChat;
import edu.java.services.LinkService;
import edu.java.services.LinkUpdater;
import edu.java.services.apihandler.ApiHandler;
import edu.java.services.apihandler.ApiHandlerResult;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
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
    private final BotHttpClient botClient;
    private final LinkService linkService;
    private final ApiHandler apiHandler;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public int update() {
        log.info("Updating...");

        Collection<Link> links = linkService.listByOldestCheck(CHECK_PER_UPDATE);
        int updatedCount = 0;
        for (var link : links) {
            ApiHandlerResult result = apiHandler.handle(link);
            if (result.hasUpdate()) {
                updatedCount++;

                botClient.update(
                    link.getLinkId(),
                    URI.create(link.getUrl()),
                    result.description(),
                    linkService.listAllByLinkId(link.getLinkId())
                        .stream()
                        .map(TgChat::getChatId)
                        .toArray(Long[]::new)
                );

                linkService.updateLastModified(link.getLinkId(), link.getLastModified());
            }
            linkService.updateLastChecked(link.getLinkId(), OffsetDateTime.now());
        }

        return updatedCount;
    }
}
