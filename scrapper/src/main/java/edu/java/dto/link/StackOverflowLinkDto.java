package edu.java.dto.link;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StackOverflowLinkDto {
    private Long linkId;

    private Integer commentsCount;

    private Integer answersCount;

    private Boolean isAnswered;
}
