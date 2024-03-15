package edu.java.services;

import edu.java.dao.model.StackOverflowLink;
import java.util.Optional;

public interface StackOverflowLinkService {
    Optional<StackOverflowLink> findById(Long id);

    void upsertLink(StackOverflowLink link);
}
