package edu.java.domain.jooq.dao;

import edu.java.domain.jooq.generated.tables.pojos.LinkStackoverflow;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JooqStackOverflowRepository {
    private final DSLContext dsl;
    private final edu.java.domain.jooq.generated.tables.LinkStackoverflow linkStackoverflowTable =
        edu.java.domain.jooq.generated.tables.LinkStackoverflow.LINK_STACKOVERFLOW;

    public Optional<LinkStackoverflow> findById(Long linkId) {
        return dsl.selectFrom(linkStackoverflowTable)
            .where(linkStackoverflowTable.LINK_ID.eq(linkId))
            .fetchOptionalInto(LinkStackoverflow.class);
    }

    public int upsertLink(LinkStackoverflow link) {
        return dsl
            .insertInto(linkStackoverflowTable)
            .columns(
                linkStackoverflowTable.LINK_ID,
                linkStackoverflowTable.IS_ANSWERED,
                linkStackoverflowTable.ANSWERS_COUNT,
                linkStackoverflowTable.COMMENTS_COUNT
            )
            .values(
                link.getLinkId(),
                link.getIsAnswered(),
                link.getAnswersCount(),
                link.getCommentsCount()
            )
            .onDuplicateKeyUpdate()
            .set(linkStackoverflowTable.IS_ANSWERED, link.getIsAnswered())
            .set(linkStackoverflowTable.ANSWERS_COUNT, link.getAnswersCount())
            .set(linkStackoverflowTable.COMMENTS_COUNT, link.getCommentsCount())
            .execute();
    }
}
