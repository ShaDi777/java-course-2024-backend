package edu.java.dao.jooq;

import edu.java.dao.jooq.generated.tables.pojos.LinkStackoverflow;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JooqStackOverflowRepository {
    private final DSLContext dsl;
    private final edu.java.dao.jooq.generated.tables.LinkStackoverflow linkStackoverflowTable =
        edu.java.dao.jooq.generated.tables.LinkStackoverflow.LINK_STACKOVERFLOW;

    public Optional<LinkStackoverflow> findById(Long linkId) {
        return dsl.selectFrom(linkStackoverflowTable)
            .where(linkStackoverflowTable.LINK_ID.eq(linkId))
            .fetchOptionalInto(LinkStackoverflow.class);
    }

    public int upsertLink(LinkStackoverflow link) {
        return dsl
            .insertInto(linkStackoverflowTable)
            .values(link)
            .onDuplicateKeyUpdate()
            .set(linkStackoverflowTable.IS_ANSWERED, link.getIsAnswered())
            .set(linkStackoverflowTable.ANSWERS_COUNT, link.getAnswersCount())
            .set(linkStackoverflowTable.COMMENTS_COUNT, link.getCommentsCount())
            .execute();
    }
}
