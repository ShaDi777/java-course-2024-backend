package edu.java.domain.jdbc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinkChat {
    private Long linkId;
    private Long chatId;
}
