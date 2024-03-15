/*
 * This file is generated by jOOQ.
 */
package edu.java.dao.jooq.generated.tables.pojos;


import java.beans.ConstructorProperties;
import java.io.Serializable;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.19.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class LinkStackoverflow implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long linkId;
    private Integer commentsCount;
    private Integer answersCount;
    private Boolean isAnswered;

    public LinkStackoverflow() {}

    public LinkStackoverflow(LinkStackoverflow value) {
        this.linkId = value.linkId;
        this.commentsCount = value.commentsCount;
        this.answersCount = value.answersCount;
        this.isAnswered = value.isAnswered;
    }

    @ConstructorProperties({ "linkId", "commentsCount", "answersCount", "isAnswered" })
    public LinkStackoverflow(
        @NotNull Long linkId,
        @Nullable Integer commentsCount,
        @Nullable Integer answersCount,
        @Nullable Boolean isAnswered
    ) {
        this.linkId = linkId;
        this.commentsCount = commentsCount;
        this.answersCount = answersCount;
        this.isAnswered = isAnswered;
    }

    /**
     * Getter for <code>public.link_stackoverflow.link_id</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getLinkId() {
        return this.linkId;
    }

    /**
     * Setter for <code>public.link_stackoverflow.link_id</code>.
     */
    public void setLinkId(@NotNull Long linkId) {
        this.linkId = linkId;
    }

    /**
     * Getter for <code>public.link_stackoverflow.comments_count</code>.
     */
    @Nullable
    public Integer getCommentsCount() {
        return this.commentsCount;
    }

    /**
     * Setter for <code>public.link_stackoverflow.comments_count</code>.
     */
    public void setCommentsCount(@Nullable Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    /**
     * Getter for <code>public.link_stackoverflow.answers_count</code>.
     */
    @Nullable
    public Integer getAnswersCount() {
        return this.answersCount;
    }

    /**
     * Setter for <code>public.link_stackoverflow.answers_count</code>.
     */
    public void setAnswersCount(@Nullable Integer answersCount) {
        this.answersCount = answersCount;
    }

    /**
     * Getter for <code>public.link_stackoverflow.is_answered</code>.
     */
    @Nullable
    public Boolean getIsAnswered() {
        return this.isAnswered;
    }

    /**
     * Setter for <code>public.link_stackoverflow.is_answered</code>.
     */
    public void setIsAnswered(@Nullable Boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final LinkStackoverflow other = (LinkStackoverflow) obj;
        if (this.linkId == null) {
            if (other.linkId != null)
                return false;
        }
        else if (!this.linkId.equals(other.linkId))
            return false;
        if (this.commentsCount == null) {
            if (other.commentsCount != null)
                return false;
        }
        else if (!this.commentsCount.equals(other.commentsCount))
            return false;
        if (this.answersCount == null) {
            if (other.answersCount != null)
                return false;
        }
        else if (!this.answersCount.equals(other.answersCount))
            return false;
        if (this.isAnswered == null) {
            if (other.isAnswered != null)
                return false;
        }
        else if (!this.isAnswered.equals(other.isAnswered))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.linkId == null) ? 0 : this.linkId.hashCode());
        result = prime * result + ((this.commentsCount == null) ? 0 : this.commentsCount.hashCode());
        result = prime * result + ((this.answersCount == null) ? 0 : this.answersCount.hashCode());
        result = prime * result + ((this.isAnswered == null) ? 0 : this.isAnswered.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LinkStackoverflow (");

        sb.append(linkId);
        sb.append(", ").append(commentsCount);
        sb.append(", ").append(answersCount);
        sb.append(", ").append(isAnswered);

        sb.append(")");
        return sb.toString();
    }
}
