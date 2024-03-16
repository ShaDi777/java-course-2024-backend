package edu.java.mapping;

import edu.java.domain.jdbc.model.StackOverflowLink;
import edu.java.domain.jooq.generated.tables.pojos.LinkStackoverflow;
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

    @Override
    public LinkStackoverflow dtoToJooqModel(StackOverflowLinkDto link) {
        LinkStackoverflow linkModel = new LinkStackoverflow();
        linkModel.setLinkId(link.getLinkId());
        linkModel.setAnswersCount(link.getAnswersCount());
        linkModel.setIsAnswered(link.getIsAnswered());
        linkModel.setCommentsCount(link.getCommentsCount());

        return linkModel;
    }

    @Override
    public StackOverflowLinkDto jooqModelToDto(LinkStackoverflow link) {
        return StackOverflowLinkDto.builder()
            .linkId(link.getLinkId())
            .answersCount(link.getAnswersCount())
            .isAnswered(link.getIsAnswered())
            .commentsCount(link.getCommentsCount())
            .build();
    }
}
