package edu.java.services.jdbc;

import edu.java.domain.jdbc.dao.JdbcLinkChatRepository;
import edu.java.domain.jdbc.dao.JdbcLinkRepository;
import edu.java.domain.jdbc.dao.JdbcTgChatRepository;
import edu.java.domain.jdbc.model.Link;
import edu.java.domain.jdbc.model.LinkChat;
import edu.java.dto.link.LinkInfoDto;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.mapping.LinkMapper;
import edu.java.services.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcTgChatRepository chatRepository;
    private final JdbcLinkRepository linkRepository;
    private final JdbcLinkChatRepository linkChatRepository;
    private final LinkMapper linkMapper;

    @Override
    public LinkInfoDto add(long tgChatId, String url) {
        if (chatRepository.findById(tgChatId).isEmpty()) {
            throw ResourceNotFoundException.chatNotFound(tgChatId);
        }

        Optional<Link> optionalLink = linkRepository.findByUrl(url);
        Link link = optionalLink.orElseGet(() -> linkRepository.save(Link.builder().url(url).build()));

        linkChatRepository.save(
            LinkChat.builder()
                .linkId(link.getLinkId())
                .chatId(tgChatId)
                .build()
        );

        return linkMapper.jdbcLinkModelToDto(link);
    }

    @Override
    public LinkInfoDto remove(long tgChatId, String url) {
        Link link = linkRepository.findByUrl(url)
            .orElseThrow(() -> ResourceNotFoundException.chatLinkNotFound(tgChatId, url));
        linkChatRepository.deleteByIds(link.getLinkId(), tgChatId);

        if (linkChatRepository.findAllChatsByLinkId(link.getLinkId()).isEmpty()) {
            linkRepository.deleteById(link.getLinkId());
        }

        return linkMapper.jdbcLinkModelToDto(link);
    }

    @Override
    public List<LinkInfoDto> listByOldestCheck(int count) {
        return linkRepository.findNLinksByOldestLastCheck(count)
            .stream()
            .map(linkMapper::jdbcLinkModelToDto)
            .toList();
    }

    @Override
    public List<LinkInfoDto> listAll() {
        return linkRepository.findAll()
            .stream()
            .map(linkMapper::jdbcLinkModelToDto)
            .toList();
    }

    @Override
    public void updateLastModified(long linkId, OffsetDateTime offsetDateTime) {
        Link link = linkRepository.findById(linkId)
            .orElseThrow(() -> ResourceNotFoundException.linkNotFound(linkId));

        link.setLastModified(offsetDateTime);
        linkRepository.updateLink(link);
    }

    @Override
    public void updateLastChecked(long linkId, OffsetDateTime offsetDateTime) {
        Link link = linkRepository.findById(linkId)
            .orElseThrow(() -> ResourceNotFoundException.linkNotFound(linkId));

        link.setLastChecked(offsetDateTime);
        linkRepository.updateLink(link);
    }
}
