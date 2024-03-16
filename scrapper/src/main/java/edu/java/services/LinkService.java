package edu.java.services;

import edu.java.dto.link.LinkInfoDto;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    LinkInfoDto add(long tgChatId, String url);

    LinkInfoDto remove(long tgChatId, String url);

    List<LinkInfoDto> listByOldestCheck(int count);

    List<LinkInfoDto> listAll();

    void updateLastModified(long linkId, OffsetDateTime offsetDateTime);

    void updateLastChecked(long linkId, OffsetDateTime offsetDateTime);
}
