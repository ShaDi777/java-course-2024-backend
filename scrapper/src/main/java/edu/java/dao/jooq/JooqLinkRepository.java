package edu.java.dao.jooq;

import edu.java.dao.jooq.generated.tables.pojos.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository {
    private final DSLContext dsl;
    private final edu.java.dao.jooq.generated.tables.Link linkTable = edu.java.dao.jooq.generated.tables.Link.LINK;

    @Transactional
    public int add(Link newLink) {
        return dsl.insertInto(linkTable)
            .set(dsl.newRecord(linkTable, newLink))
            .execute();
    }

    @Transactional
    public Optional<Link> findById(Long linkId) {
        return dsl.selectFrom(linkTable)
            .where(linkTable.LINK_ID.eq(linkId))
            .fetchOptionalInto(Link.class);
    }

    @Transactional
    public Optional<Link> findByUrl(String url) {
        return dsl.selectFrom(linkTable)
            .where(linkTable.URL.eq(url))
            .fetchOptionalInto(Link.class);
    }

    @Transactional
    public List<Link> findNByOldestLastCheck(int count) {
        return dsl.selectFrom(linkTable)
            .orderBy(linkTable.LAST_CHECKED.asc())
            .limit(count)
            .fetchInto(Link.class);
    }

    @Transactional
    public List<Link> findAll() {
        return dsl.selectFrom(linkTable)
            .fetchInto(Link.class);
    }

    @Transactional
    public int updateLastModified(Long linkId, OffsetDateTime newModificationTime) {
        return dsl.update(linkTable)
            .set(linkTable.LAST_MODIFIED, newModificationTime)
            .where(linkTable.LINK_ID.eq(linkId))
            .execute();
    }

    @Transactional
    public int updateLastChecked(Long linkId, OffsetDateTime newCheckTime) {
        return dsl.update(linkTable)
            .set(linkTable.LAST_CHECKED, newCheckTime)
            .where(linkTable.LINK_ID.eq(linkId))
            .execute();
    }

    @Transactional
    public int deleteById(Long linkId) {
        return dsl.deleteFrom(linkTable)
            .where(linkTable.LINK_ID.eq(linkId))
            .execute();
    }

    @Transactional
    public int deleteByUrl(String url) {
        return dsl.deleteFrom(linkTable)
            .where(linkTable.URL.eq(url))
            .execute();
    }
}
