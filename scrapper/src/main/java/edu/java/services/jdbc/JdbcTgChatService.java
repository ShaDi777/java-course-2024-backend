package edu.java.services.jdbc;

import edu.java.domain.jdbc.dao.JdbcLinkChatRepository;
import edu.java.domain.jdbc.dao.JdbcLinkRepository;
import edu.java.domain.jdbc.dao.JdbcTgChatRepository;
import edu.java.domain.jdbc.model.Link;
import edu.java.domain.jdbc.model.TgChat;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.services.TgChatService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final JdbcLinkRepository linkRepository;
    private final JdbcTgChatRepository chatRepository;
    private final JdbcLinkChatRepository linkChatRepository;

    @Override
    public void register(long tgChatId) {
        if (chatRepository.findById(tgChatId).isPresent()) {
            throw new ChatAlreadyExistsException();
        }

        chatRepository.save(new TgChat(tgChatId));
    }

    @Override
    public void unregister(long tgChatId) {
        if (chatRepository.findById(tgChatId).isEmpty()) {
            throw ResourceNotFoundException.chatNotFound(tgChatId);
        }

        Collection<Link> links = linkChatRepository.findAllLinksByChatId(tgChatId);
        linkChatRepository.deleteAllByChatId(tgChatId);
        for (var link : links) {
            long linkId = link.getLinkId();
            if (linkChatRepository.findAllChatsByLinkId(linkId).isEmpty()) {
                linkRepository.deleteById(linkId);
            }
        }

        chatRepository.deleteById(tgChatId);
    }
}
