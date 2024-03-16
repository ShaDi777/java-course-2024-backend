package edu.java.dto.link;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LinkInfoDto {
    private Long linkId;
    private String url;
    private OffsetDateTime lastModified;
    private OffsetDateTime lastChecked;
}
