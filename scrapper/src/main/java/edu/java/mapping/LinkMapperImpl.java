package edu.java.mapping;

import edu.java.domain.jdbc.model.Link;
import edu.java.dto.link.LinkInfoDto;
import edu.java.dto.link.LinkResponse;
import java.net.URI;
import org.springframework.stereotype.Component;

@Component
public class LinkMapperImpl implements LinkMapper {
    @Override
    public LinkResponse linkInfoDtoToResponse(LinkInfoDto link) {
        return new LinkResponse(link.getLinkId(), URI.create(link.getUrl()));
    }

    @Override
    public LinkInfoDto jdbcLinkModelToDto(Link link) {
        return new LinkInfoDto(
            link.getLinkId(),
            link.getUrl(),
            link.getLastModified(),
            link.getLastChecked()
        );
    }
}
