package edu.java.services.jooq;

import edu.java.domain.jooq.dao.JooqLinkChatRepository;
import edu.java.domain.jooq.dao.JooqLinkRepository;
import edu.java.domain.jooq.dao.JooqTgChatRepository;
import edu.java.domain.jooq.generated.tables.pojos.Link;
import edu.java.domain.jooq.generated.tables.pojos.LinkChat;
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
public class JooqLinkService implements LinkService {
    private final JooqTgChatRepository chatRepository;
    private final JooqLinkRepository linkRepository;
    private final JooqLinkChatRepository linkChatRepository;
    private final LinkMapper linkMapper;

    @Override
    public LinkInfoDto add(long tgChatId, String url) {
        if (chatRepository.getById(tgChatId).isEmpty()) {
            throw ResourceNotFoundException.chatNotFound(tgChatId);
        }

        Link linkToInsert = new Link();
        linkToInsert.setUrl(url);

        Optional<Link> optionalLink = linkRepository.findByUrl(url);
        Link link = optionalLink.orElseGet(() -> linkRepository.add(linkToInsert));

        LinkChat linkChat = new LinkChat();
        linkChat.setLinkId(link.getLinkId());
        linkChat.setChatId(tgChatId);
        linkChatRepository.add(linkChat);

        return linkMapper.jooqLinkModelToDto(link);
    }

    @Override
    public LinkInfoDto remove(long tgChatId, String url) {
        Link link = linkRepository.findByUrl(url)
            .orElseThrow(() -> ResourceNotFoundException.chatLinkNotFound(tgChatId, url));
        linkChatRepository.delete(tgChatId, link.getLinkId());

        if (linkChatRepository.findAllByLinkId(link.getLinkId()).isEmpty()) {
            linkRepository.deleteById(link.getLinkId());
        }

        return linkMapper.jooqLinkModelToDto(link);
    }

    @Override
    public List<LinkInfoDto> listByOldestCheck(int count) {
        return linkRepository.findNByOldestLastCheck(count)
            .stream()
            .map(linkMapper::jooqLinkModelToDto)
            .toList();
    }

    @Override
    public List<LinkInfoDto> listAll() {
        return linkRepository.findAll()
            .stream()
            .map(linkMapper::jooqLinkModelToDto)
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
