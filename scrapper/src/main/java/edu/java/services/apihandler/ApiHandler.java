package edu.java.services.apihandler;

import edu.java.dao.model.Link;

public interface ApiHandler {
    ApiHandlerResult handle(Link link);

    boolean supports(Link link);

    void setNext(ApiHandler next);
}
