package edu.java.mapping;

import edu.java.domain.jdbc.model.StackOverflowLink;
import edu.java.domain.jooq.generated.tables.pojos.LinkStackoverflow;
import edu.java.dto.link.StackOverflowLinkDto;

public interface StackOverflowLinkMapper {
    StackOverflowLink dtoToJdbcModel(StackOverflowLinkDto link);

    StackOverflowLinkDto jdbcModelToDto(StackOverflowLink link);

    LinkStackoverflow dtoToJooqModel(StackOverflowLinkDto link);

    StackOverflowLinkDto jooqModelToDto(LinkStackoverflow link);
}
