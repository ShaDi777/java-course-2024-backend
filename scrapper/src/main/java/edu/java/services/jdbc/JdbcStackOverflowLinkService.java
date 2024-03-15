package edu.java.services.jdbc;

import edu.java.dao.jdbc.JdbcStackOverflowLinkRepository;
import edu.java.dao.model.StackOverflowLink;
import edu.java.services.StackOverflowLinkService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JdbcStackOverflowLinkService implements StackOverflowLinkService {
    private final JdbcStackOverflowLinkRepository stackOverflowLinkRepository;

    @Override
    public Optional<StackOverflowLink> findById(Long id) {
        return stackOverflowLinkRepository.findById(id);
    }

    @Override
    public void upsertLink(StackOverflowLink link) {
        stackOverflowLinkRepository.upsertLink(link);
    }
}
