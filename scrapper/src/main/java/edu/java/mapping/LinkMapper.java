package edu.java.mapping;

import edu.java.controllers.dto.LinkResponse;
import edu.java.dao.model.Link;

public interface LinkMapper {
    LinkResponse linkToResponse(Link link);
}
