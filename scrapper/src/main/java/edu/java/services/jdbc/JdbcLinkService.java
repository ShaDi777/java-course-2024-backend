package edu.java.services.jdbc;

import edu.java.dao.jdbc.JdbcLinkRepository;
import edu.java.dao.model.Link;
import edu.java.dao.model.TgChat;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.Collection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;

    @Override
    public Link add(long tgChatId, String url) {
        return linkRepository.add(tgChatId, url);
    }

    @Override
    public Link remove(long tgChatId, String url) {
        return linkRepository.remove(tgChatId, url);
    }

    @Override
    public Collection<Link> listAllByChatId(long tgChatId) {
        return linkRepository.findAllLinksByChatId(tgChatId);
    }

    @Override
    public Collection<TgChat> listAllByLinkId(long linkId) {
        return linkRepository.findAllChatsByLinkId(linkId);
    }

    @Override
    public Collection<Link> listByOldestCheck(int count) {
        return linkRepository.findNLinksByOldestLastCheck(count);
    }

    @Override
    public Collection<Link> listAll() {
        return linkRepository.findAll();
    }

    @Override
    public void updateLastModified(long linkId, OffsetDateTime offsetDateTime) {
        linkRepository.updateLastModified(linkId, offsetDateTime);
    }

    @Override
    public void updateLastChecked(long linkId, OffsetDateTime offsetDateTime) {
        linkRepository.updateLastChecked(linkId, offsetDateTime);
    }
}
