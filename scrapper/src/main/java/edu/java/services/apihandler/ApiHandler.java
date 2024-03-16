package edu.java.services.apihandler;

import edu.java.dto.link.LinkInfoDto;

public interface ApiHandler {
    ApiHandlerResult handle(LinkInfoDto link);

    boolean supports(String linkUrl);

    void setNext(ApiHandler next);
}
