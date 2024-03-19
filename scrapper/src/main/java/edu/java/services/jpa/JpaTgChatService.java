package edu.java.services.jpa;

import edu.java.domain.jpa.dao.JpaLinkRepository;
import edu.java.domain.jpa.dao.JpaTgChatRepository;
import edu.java.domain.jpa.model.Chat;
import edu.java.exceptions.ChatAlreadyExistsException;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.services.TgChatService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class JpaTgChatService implements TgChatService {
    private final JpaTgChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;

    @Override
    public void register(long tgChatId) {
        if (chatRepository.findById(tgChatId).isPresent()) {
            throw new ChatAlreadyExistsException();
        }

        chatRepository.save(new Chat(tgChatId, new ArrayList<>()));
    }

    @Override
    public void unregister(long tgChatId) {
        if (chatRepository.findById(tgChatId).isEmpty()) {
            throw ResourceNotFoundException.chatNotFound(tgChatId);
        }

        linkRepository.deleteAllFromLinkChatsByChatId(tgChatId);

        chatRepository.deleteById(tgChatId);
    }
}
