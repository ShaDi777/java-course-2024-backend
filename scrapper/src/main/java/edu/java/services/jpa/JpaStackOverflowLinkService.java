package edu.java.services.jpa;

import edu.java.domain.jpa.dao.JpaStackOverflowRepository;
import edu.java.dto.link.StackOverflowLinkDto;
import edu.java.mapping.StackOverflowLinkMapper;
import edu.java.services.StackOverflowLinkService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class JpaStackOverflowLinkService implements StackOverflowLinkService {
    private final JpaStackOverflowRepository stackOverflowRepository;
    private final StackOverflowLinkMapper stackOverflowLinkMapper;

    @Override
    public Optional<StackOverflowLinkDto> findById(Long id) {
        return stackOverflowRepository.findById(id).map(stackOverflowLinkMapper::jpaModelToDto);
    }

    @Override
    public void upsertLink(StackOverflowLinkDto link) {
        stackOverflowRepository.save(stackOverflowLinkMapper.dtoToJpaModel(link));
    }
}
