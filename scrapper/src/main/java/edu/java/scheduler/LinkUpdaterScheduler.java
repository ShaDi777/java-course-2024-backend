package edu.java.scheduler;

import edu.java.dto.bot.BotLinkUpdateRequest;
import edu.java.services.LinkUpdaterService;
import edu.java.services.sender.UpdateSender;
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
public class LinkUpdaterScheduler {
    private final UpdateSender updateSender;
    private final LinkUpdaterService linkUpdaterService;

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public int update() {
        log.info("Updating...");

        List<BotLinkUpdateRequest> links = linkUpdaterService.update();
        links.forEach(updateSender::send);

        return links.size();
    }
}
