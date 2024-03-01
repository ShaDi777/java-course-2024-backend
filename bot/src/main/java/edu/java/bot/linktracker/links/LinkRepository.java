package edu.java.bot.linktracker.links;

import java.util.List;
import org.apache.commons.validator.routines.UrlValidator;

public interface LinkRepository {
    default boolean isValidLink(String link) {
        var validator = new UrlValidator();
        return validator.isValid(link);
    }

    List<String> getTrackedLinks(long telegramId);

    boolean trackLink(long telegramId, String link);

    boolean untrackLink(long telegramId, String link);
}
