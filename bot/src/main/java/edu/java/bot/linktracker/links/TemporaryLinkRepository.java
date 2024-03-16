package edu.java.bot.linktracker.links;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemporaryLinkRepository implements LinkRepository {
    private final Map<Long, List<String>> map = new HashMap<>();

    @Override
    public List<String> getTrackedLinks(long telegramId) {
        return map.getOrDefault(telegramId, List.of());
    }

    @Override
    public boolean trackLink(long telegramId, String link) {
        if (!map.containsKey(telegramId)) {
            map.put(telegramId, new ArrayList<>());
        }
        var list = map.get(telegramId);
        list.add(link);
        return true;
    }

    @Override
    public boolean untrackLink(long telegramId, String link) {
        var list = map.get(telegramId);
        if (list != null) {
            list.remove(link);
        }

        return true;
    }
}
