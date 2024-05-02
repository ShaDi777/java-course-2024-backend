package edu.java.services.jooq;

import edu.java.domain.jooq.dao.JooqLinkChatRepository;
import edu.java.domain.jooq.dao.JooqLinkRepository;
import edu.java.domain.jooq.dao.JooqTgChatRepository;
import edu.java.domain.jooq.generated.tables.pojos.Chat;
import edu.java.domain.jooq.generated.tables.pojos.LinkChat;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.services.TgChatService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JooqTgChatService implements TgChatService {
    private final JooqLinkRepository linkRepository;
    private final JooqTgChatRepository chatRepository;
    private final JooqLinkChatRepository linkChatRepository;

    @Override
    public void register(long tgChatId) {
        if (chatRepository.getById(tgChatId).isPresent()) {
            throw new ChatAlreadyExistsException();
        }

        Chat chat = new Chat();
        chat.setChatId(tgChatId);
        chatRepository.add(chat);
    }

    @Override
    public void unregister(long tgChatId) {
        if (chatRepository.getById(tgChatId).isEmpty()) {
            throw ResourceNotFoundException.chatNotFound(tgChatId);
        }

        Collection<LinkChat> links = linkChatRepository.findAllByChatId(tgChatId);
        linkChatRepository.deleteAllByChatId(tgChatId);
        for (var link : links) {
            long linkId = link.getLinkId();
            if (linkChatRepository.findAllByLinkId(linkId).isEmpty()) {
                linkRepository.deleteById(linkId);
            }
        }

        chatRepository.deleteById(tgChatId);
    }
}
