package edu.java.dao.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StackOverflowLink {
    private Long linkId;

    private Integer commentsCount;

    private Integer answersCount;

    private Boolean isAnswered;
}
