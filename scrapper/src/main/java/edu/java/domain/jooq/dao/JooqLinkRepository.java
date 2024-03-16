package edu.java.domain.jooq.dao;

import edu.java.domain.jooq.generated.tables.pojos.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository {
    private final DSLContext dsl;
    private final edu.java.domain.jooq.generated.tables.Link linkTable =
        edu.java.domain.jooq.generated.tables.Link.LINK;

    public Link add(Link newLink) {
        dsl.insertInto(linkTable)
            .columns(linkTable.URL)
            .values(newLink.getUrl())
            .onConflictDoNothing()
            .execute();

        return findByUrl(newLink.getUrl()).get();
    }

    public Optional<Link> findById(Long linkId) {
        return dsl.selectFrom(linkTable)
            .where(linkTable.LINK_ID.eq(linkId))
            .fetchOptionalInto(Link.class);
    }

    public Optional<Link> findByUrl(String url) {
        return dsl.selectFrom(linkTable)
            .where(linkTable.URL.eq(url))
            .fetchOptionalInto(Link.class);
    }

    public List<Link> findNByOldestLastCheck(int count) {
        return dsl.selectFrom(linkTable)
            .orderBy(linkTable.LAST_CHECKED.asc())
            .limit(count)
            .fetchInto(Link.class);
    }

    public List<Link> findAll() {
        return dsl.selectFrom(linkTable)
            .fetchInto(Link.class);
    }

    public int updateLink(Link link) {
        return dsl.update(linkTable)
            .set(linkTable.LAST_MODIFIED, link.getLastModified())
            .set(linkTable.LAST_CHECKED, link.getLastChecked())
            .where(linkTable.LINK_ID.eq(link.getLinkId()))
            .execute();
    }

    public int deleteById(Long linkId) {
        return dsl.deleteFrom(linkTable)
            .where(linkTable.LINK_ID.eq(linkId))
            .execute();
    }

    public int deleteByUrl(String url) {
        return dsl.deleteFrom(linkTable)
            .where(linkTable.URL.eq(url))
            .execute();
    }
}
