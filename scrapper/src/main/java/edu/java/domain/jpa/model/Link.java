package edu.java.domain.jpa.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.PastOrPresent;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    @Id
    @SequenceGenerator(name = "link_seq", sequenceName = "link_link_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "link_seq", strategy = GenerationType.SEQUENCE)
    @Column(name = "link_id")
    private Long linkId;

    @NonNull
    @Column(name = "url", unique = true)
    private String url;

    @PastOrPresent
    @Column(name = "last_modified")
    private OffsetDateTime lastModified;

    @PastOrPresent
    @Column(name = "last_checked")
    private OffsetDateTime lastChecked;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "link_chat",
        joinColumns = @JoinColumn(name = "link_id"),
        inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private List<Chat> chats;
}
