package edu.java.services.jdbc;

import edu.java.domain.jdbc.dao.JdbcLinkChatRepository;
import edu.java.dto.chat.TgChatInfoDto;
import edu.java.dto.link.LinkInfoDto;
import edu.java.mapping.LinkMapper;
import edu.java.services.LinkChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcLinkChatService implements LinkChatService {
    private final JdbcLinkChatRepository linkChatRepository;
    private final LinkMapper linkMapper;

    @Override
    public List<LinkInfoDto> listAllLinksByChatId(long tgChatId) {
        return linkChatRepository.findAllLinksByChatId(tgChatId)
            .stream()
            .map(linkMapper::jdbcLinkModelToDto)
            .toList();
    }

    @Override
    public List<TgChatInfoDto> listAllChatsByLinkId(long linkId) {
        return linkChatRepository.findAllChatsByLinkId(linkId)
            .stream()
            .map(jdbcTgChat -> new TgChatInfoDto(jdbcTgChat.getChatId()))
            .toList();
    }
}
