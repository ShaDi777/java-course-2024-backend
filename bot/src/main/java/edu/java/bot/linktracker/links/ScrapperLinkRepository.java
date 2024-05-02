package edu.java.bot.linktracker.links;

import edu.java.bot.client.ScrapperLinksHttpClient;
import edu.java.bot.services.validator.LinkValidator;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScrapperLinkRepository implements LinkRepository {
    private final ScrapperLinksHttpClient linksHttpClient;
    private final LinkValidator linkValidator;

    @Override
    public boolean isValidLink(String link) {
        return LinkRepository.super.isValidLink(link) && linkValidator.isLinkValid(link);
    }

    @Override
    public List<String> getTrackedLinks(long telegramId) {
        return Arrays.stream(linksHttpClient.getLinks(telegramId).links()).map(x -> x.url().toString()).toList();
    }

    @Override
    public boolean trackLink(long telegramId, String link) {
        linksHttpClient.addLink(telegramId, link);
        return true;
    }

    @Override
    public boolean untrackLink(long telegramId, String link) {
        linksHttpClient.deleteLink(telegramId, link);
        return true;
    }
}
