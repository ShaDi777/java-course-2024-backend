package edu.java.services.jooq;

import edu.java.domain.jooq.dao.JooqStackOverflowRepository;
import edu.java.dto.link.StackOverflowLinkDto;
import edu.java.mapping.StackOverflowLinkMapper;
import edu.java.services.StackOverflowLinkService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class JooqStackOverflowService implements StackOverflowLinkService {
    private final JooqStackOverflowRepository stackOverflowLinkRepository;
    private final StackOverflowLinkMapper stackOverflowLinkMapper;

    @Override
    public Optional<StackOverflowLinkDto> findById(Long id) {
        return stackOverflowLinkRepository.findById(id).map(stackOverflowLinkMapper::jooqModelToDto);
    }

    @Override
    public void upsertLink(StackOverflowLinkDto link) {
        stackOverflowLinkRepository.upsertLink(stackOverflowLinkMapper.dtoToJooqModel(link));
    }
}
