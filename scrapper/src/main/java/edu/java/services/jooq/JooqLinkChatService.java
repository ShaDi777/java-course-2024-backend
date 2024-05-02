package edu.java.services.jooq;

import edu.java.domain.jooq.dao.JooqLinkChatRepository;
import edu.java.domain.jooq.dao.JooqLinkRepository;
import edu.java.domain.jooq.generated.tables.pojos.LinkChat;
import edu.java.dto.chat.TgChatInfoDto;
import edu.java.dto.link.LinkInfoDto;
import edu.java.mapping.LinkMapper;
import edu.java.services.LinkChatService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JooqLinkChatService implements LinkChatService {
    private final JooqLinkRepository linkRepository;
    private final JooqLinkChatRepository linkChatRepository;
    private final LinkMapper linkMapper;

    @Override
    public List<LinkInfoDto> listAllLinksByChatId(long tgChatId) {
        return linkChatRepository.findAllByChatId(tgChatId)
            .stream()
            .map(LinkChat::getLinkId)
            .map(linkRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(linkMapper::jooqLinkModelToDto)
            .toList();
    }

    @Override
    public List<TgChatInfoDto> listAllChatsByLinkId(long linkId) {
        return linkChatRepository.findAllByLinkId(linkId)
            .stream()
            .map(jdbcTgChat -> new TgChatInfoDto(jdbcTgChat.getChatId()))
            .toList();
    }
}
