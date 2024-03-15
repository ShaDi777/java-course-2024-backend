package edu.java.services;

import edu.java.dao.model.Link;
import edu.java.dao.model.TgChat;
import java.time.OffsetDateTime;
import java.util.Collection;

public interface LinkService {
    Link add(long tgChatId, String url);

    Link remove(long tgChatId, String url);

    Collection<Link> listAllByChatId(long tgChatId);

    Collection<TgChat> listAllByLinkId(long linkId);

    Collection<Link> listByOldestCheck(int count);

    Collection<Link> listAll();

    void updateLastModified(long linkId, OffsetDateTime offsetDateTime);

    void updateLastChecked(long linkId, OffsetDateTime offsetDateTime);
}
