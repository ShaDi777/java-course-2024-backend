package edu.java.mapping;

import edu.java.domain.jdbc.model.Link;
import edu.java.dto.link.LinkInfoDto;
import edu.java.dto.link.LinkResponse;

public interface LinkMapper {
    LinkResponse linkInfoDtoToResponse(LinkInfoDto link);

    LinkInfoDto jdbcLinkModelToDto(Link link);
}
