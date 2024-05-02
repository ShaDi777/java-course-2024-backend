/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.generated.tables.records;


import edu.java.domain.jooq.generated.tables.Link;

import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


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
public class LinkRecord extends UpdatableRecordImpl<LinkRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.link.link_id</code>.
     */
    public void setLinkId(@Nullable Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.link.link_id</code>.
     */
    @Nullable
    public Long getLinkId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.link.url</code>.
     */
    public void setUrl(@NotNull String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.link.url</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public String getUrl() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.link.last_modified</code>.
     */
    public void setLastModified(@Nullable OffsetDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.link.last_modified</code>.
     */
    @Nullable
    public OffsetDateTime getLastModified() {
        return (OffsetDateTime) get(2);
    }

    /**
     * Setter for <code>public.link.last_checked</code>.
     */
    public void setLastChecked(@Nullable OffsetDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.link.last_checked</code>.
     */
    @Nullable
    public OffsetDateTime getLastChecked() {
        return (OffsetDateTime) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LinkRecord
     */
    public LinkRecord() {
        super(Link.LINK);
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    @ConstructorProperties({ "linkId", "url", "lastModified", "lastChecked" })
    public LinkRecord(@Nullable Long linkId, @NotNull String url, @Nullable OffsetDateTime lastModified, @Nullable OffsetDateTime lastChecked) {
        super(Link.LINK);

        setLinkId(linkId);
        setUrl(url);
        setLastModified(lastModified);
        setLastChecked(lastChecked);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    public LinkRecord(edu.java.domain.jooq.generated.tables.pojos.Link value) {
        super(Link.LINK);

        if (value != null) {
            setLinkId(value.getLinkId());
            setUrl(value.getUrl());
            setLastModified(value.getLastModified());
            setLastChecked(value.getLastChecked());
            resetChangedOnNotNull();
        }
    }
}
