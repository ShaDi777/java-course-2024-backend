package edu.java.domain.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "link_stackoverflow")
public class StackOverflowLink {
    @Id
    @Column(name = "link_id")
    private Long linkId;

    @Column(name = "comments_count")
    private Integer commentsCount;

    @Column(name = "answers_count")
    private Integer answersCount;

    @Column(name = "is_answered")
    private Boolean isAnswered;
}
