package edu.java.dao.model;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Link {
    private Long linkId;

    private String url;

    private OffsetDateTime lastModified;

    private OffsetDateTime lastChecked;
}
