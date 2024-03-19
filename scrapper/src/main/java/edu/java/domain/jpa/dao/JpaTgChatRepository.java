package edu.java.domain.jpa.dao;

import edu.java.domain.jpa.model.Chat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTgChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByLinksLinkId(Long linkId);
}
