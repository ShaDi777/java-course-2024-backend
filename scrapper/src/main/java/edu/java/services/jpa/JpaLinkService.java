package edu.java.services.jpa;

import edu.java.domain.jpa.dao.JpaLinkRepository;
import edu.java.domain.jpa.dao.JpaStackOverflowRepository;
import edu.java.domain.jpa.dao.JpaTgChatRepository;
import edu.java.domain.jpa.model.Chat;
import edu.java.domain.jpa.model.Link;
import edu.java.dto.link.LinkInfoDto;
import edu.java.exceptions.ResourceNotFoundException;
import edu.java.mapping.LinkMapper;
import edu.java.services.LinkService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaTgChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;
    private final JpaStackOverflowRepository stackOverflowRepository;
    private final LinkMapper linkMapper;

    @Transactional
    @Override
    public LinkInfoDto add(long tgChatId, String url) {
        Optional<Chat> chatOptional = chatRepository.findById(tgChatId);
        if (chatOptional.isEmpty()) {
            throw ResourceNotFoundException.chatNotFound(tgChatId);
        }

        Link link = linkRepository.findByUrl(url)
            .orElseGet(() -> linkRepository.saveAndFlush(
                Link.builder()
                    .url(url)
                    .lastChecked(OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC))
                    .lastModified(OffsetDateTime.now())
                    .build())
            );

        linkRepository.insertLinkChat(link.getLinkId(), tgChatId);

        return linkMapper.jpaLinkModelToDto(link);
    }

    @Transactional
    @Override
    public LinkInfoDto remove(long tgChatId, String url) {
        Optional<Chat> chatOptional = chatRepository.findById(tgChatId);
        if (chatOptional.isEmpty()) {
            throw ResourceNotFoundException.chatNotFound(tgChatId);
        }

        Link link = linkRepository.findByUrl(url)
            .orElseThrow(() -> ResourceNotFoundException.chatLinkNotFound(tgChatId, url));

        Long linkId = link.getLinkId();
        linkRepository.removeLinkChat(linkId, tgChatId);
        if (chatRepository.findAllByLinksLinkId(linkId).isEmpty()) {
            stackOverflowRepository.deleteById(linkId);
            linkRepository.deleteById(linkId);
        }

        return linkMapper.jpaLinkModelToDto(link);
    }

    @Override
    public List<LinkInfoDto> listByOldestCheck(int count) {
        return linkRepository.findByOrderByLastCheckedAsc(Limit.of(count))
            .stream()
            .map(linkMapper::jpaLinkModelToDto)
            .toList();
    }

    @Override
    public List<LinkInfoDto> listAll() {
        return linkRepository.findAll()
            .stream()
            .map(linkMapper::jpaLinkModelToDto)
            .toList();
    }

    @Transactional
    @Override
    public void updateLastModified(long linkId, OffsetDateTime offsetDateTime) {
        Optional<Link> linkOptional = linkRepository.findById(linkId);
        if (linkOptional.isEmpty()) {
            throw ResourceNotFoundException.linkNotFound(linkId);
        }

        Link link = linkOptional.get();
        link.setLastModified(offsetDateTime);
        linkRepository.save(link);
    }

    @Transactional
    @Override
    public void updateLastChecked(long linkId, OffsetDateTime offsetDateTime) {
        Optional<Link> linkOptional = linkRepository.findById(linkId);
        if (linkOptional.isEmpty()) {
            throw ResourceNotFoundException.linkNotFound(linkId);
        }

        Link link = linkOptional.get();
        link.setLastChecked(offsetDateTime);
        linkRepository.save(link);
    }
}
