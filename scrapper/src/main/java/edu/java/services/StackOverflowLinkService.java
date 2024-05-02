package edu.java.services;

import edu.java.dto.link.StackOverflowLinkDto;
import java.util.Optional;

public interface StackOverflowLinkService {
    Optional<StackOverflowLinkDto> findById(Long id);

    void upsertLink(StackOverflowLinkDto link);
}
