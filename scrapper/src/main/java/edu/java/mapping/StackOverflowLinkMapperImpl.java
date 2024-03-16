package edu.java.mapping;

import edu.java.domain.jdbc.model.StackOverflowLink;
import edu.java.dto.link.StackOverflowLinkDto;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowLinkMapperImpl implements StackOverflowLinkMapper {
    @Override
    public StackOverflowLink dtoToJdbcModel(StackOverflowLinkDto link) {
        return StackOverflowLink.builder()
            .linkId(link.getLinkId())
            .answersCount(link.getAnswersCount())
            .isAnswered(link.getIsAnswered())
            .commentsCount(link.getCommentsCount())
            .build();
    }

    @Override
    public StackOverflowLinkDto jdbcModelToDto(StackOverflowLink link) {
        return StackOverflowLinkDto.builder()
            .linkId(link.getLinkId())
            .answersCount(link.getAnswersCount())
            .isAnswered(link.getIsAnswered())
            .commentsCount(link.getCommentsCount())
            .build();
    }
}
