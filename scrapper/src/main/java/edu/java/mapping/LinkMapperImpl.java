package edu.java.mapping;

import edu.java.controllers.dto.LinkResponse;
import edu.java.dao.model.Link;
import java.net.URI;
import org.springframework.stereotype.Component;

@Component
public class LinkMapperImpl implements LinkMapper {
    @Override
    public LinkResponse linkToResponse(Link link) {
        return new LinkResponse(link.getLinkId(), URI.create(link.getUrl()));
    }
}
