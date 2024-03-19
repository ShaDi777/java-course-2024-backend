package edu.java.services.jpa;

import edu.java.domain.jpa.dao.JpaLinkRepository;
import edu.java.domain.jpa.dao.JpaTgChatRepository;
import edu.java.dto.chat.TgChatInfoDto;
import edu.java.dto.link.LinkInfoDto;
import edu.java.mapping.LinkMapper;
import edu.java.services.LinkChatService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class JpaLinkChatService implements LinkChatService {
    private final JpaTgChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;
    private final LinkMapper linkMapper;

    @Override
    public List<LinkInfoDto> listAllLinksByChatId(long tgChatId) {
        return linkRepository.findAllByChatsChatId(tgChatId)
            .stream()
            .map(linkMapper::jpaLinkModelToDto)
            .toList();
    }

    @Override
    public List<TgChatInfoDto> listAllChatsByLinkId(long linkId) {
        return chatRepository.findAllByLinksLinkId(linkId)
            .stream()
            .map(jpaChat -> new TgChatInfoDto(jpaChat.getChatId()))
            .toList();
    }
}
