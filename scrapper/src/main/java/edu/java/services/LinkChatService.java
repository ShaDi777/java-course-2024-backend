package edu.java.services;

import edu.java.dto.chat.TgChatInfoDto;
import edu.java.dto.link.LinkInfoDto;
import java.util.List;

public interface LinkChatService {
    List<LinkInfoDto> listAllLinksByChatId(long tgChatId);

    List<TgChatInfoDto> listAllChatsByLinkId(long linkId);
}
