package edu.java.mapping;

import edu.java.domain.jdbc.model.StackOverflowLink;
import edu.java.domain.jooq.generated.tables.pojos.LinkStackoverflow;
import edu.java.dto.link.StackOverflowLinkDto;

public interface StackOverflowLinkMapper {
    StackOverflowLink dtoToJdbcModel(StackOverflowLinkDto link);

    StackOverflowLinkDto jdbcModelToDto(StackOverflowLink link);

    LinkStackoverflow dtoToJooqModel(StackOverflowLinkDto link);

    StackOverflowLinkDto jooqModelToDto(LinkStackoverflow link);

    edu.java.domain.jpa.model.StackOverflowLink dtoToJpaModel(StackOverflowLinkDto link);

    StackOverflowLinkDto jpaModelToDto(edu.java.domain.jpa.model.StackOverflowLink link);
}
