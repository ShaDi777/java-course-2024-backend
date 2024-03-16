package edu.java.services.jdbc;

import edu.java.domain.jdbc.dao.JdbcStackOverflowLinkRepository;
import edu.java.dto.link.StackOverflowLinkDto;
import edu.java.mapping.StackOverflowLinkMapper;
import edu.java.services.StackOverflowLinkService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcStackOverflowLinkService implements StackOverflowLinkService {
    private final JdbcStackOverflowLinkRepository stackOverflowLinkRepository;
    private final StackOverflowLinkMapper stackOverflowLinkMapper;

    @Override
    public Optional<StackOverflowLinkDto> findById(Long id) {
        return stackOverflowLinkRepository.findById(id).map(stackOverflowLinkMapper::jdbcModelToDto);
    }

    @Override
    public void upsertLink(StackOverflowLinkDto link) {
        stackOverflowLinkRepository.upsertLink(stackOverflowLinkMapper.dtoToJdbcModel(link));
    }
}
