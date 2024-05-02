/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.generated.tables.pojos;


import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.OffsetDateTime;

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
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long linkId;
    private String url;
    private OffsetDateTime lastModified;
    private OffsetDateTime lastChecked;

    public Link() {}

    public Link(Link value) {
        this.linkId = value.linkId;
        this.url = value.url;
        this.lastModified = value.lastModified;
        this.lastChecked = value.lastChecked;
    }

    @ConstructorProperties({ "linkId", "url", "lastModified", "lastChecked" })
    public Link(
        @Nullable Long linkId,
        @NotNull String url,
        @Nullable OffsetDateTime lastModified,
        @Nullable OffsetDateTime lastChecked
    ) {
        this.linkId = linkId;
        this.url = url;
        this.lastModified = lastModified;
        this.lastChecked = lastChecked;
    }

    /**
     * Getter for <code>public.link.link_id</code>.
     */
    @Nullable
    public Long getLinkId() {
        return this.linkId;
    }

    /**
     * Setter for <code>public.link.link_id</code>.
     */
    public void setLinkId(@Nullable Long linkId) {
        this.linkId = linkId;
    }

    /**
     * Getter for <code>public.link.url</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public String getUrl() {
        return this.url;
    }

    /**
     * Setter for <code>public.link.url</code>.
     */
    public void setUrl(@NotNull String url) {
        this.url = url;
    }

    /**
     * Getter for <code>public.link.last_modified</code>.
     */
    @Nullable
    public OffsetDateTime getLastModified() {
        return this.lastModified;
    }

    /**
     * Setter for <code>public.link.last_modified</code>.
     */
    public void setLastModified(@Nullable OffsetDateTime lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Getter for <code>public.link.last_checked</code>.
     */
    @Nullable
    public OffsetDateTime getLastChecked() {
        return this.lastChecked;
    }

    /**
     * Setter for <code>public.link.last_checked</code>.
     */
    public void setLastChecked(@Nullable OffsetDateTime lastChecked) {
        this.lastChecked = lastChecked;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Link other = (Link) obj;
        if (this.linkId == null) {
            if (other.linkId != null)
                return false;
        }
        else if (!this.linkId.equals(other.linkId))
            return false;
        if (this.url == null) {
            if (other.url != null)
                return false;
        }
        else if (!this.url.equals(other.url))
            return false;
        if (this.lastModified == null) {
            if (other.lastModified != null)
                return false;
        }
        else if (!this.lastModified.equals(other.lastModified))
            return false;
        if (this.lastChecked == null) {
            if (other.lastChecked != null)
                return false;
        }
        else if (!this.lastChecked.equals(other.lastChecked))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.linkId == null) ? 0 : this.linkId.hashCode());
        result = prime * result + ((this.url == null) ? 0 : this.url.hashCode());
        result = prime * result + ((this.lastModified == null) ? 0 : this.lastModified.hashCode());
        result = prime * result + ((this.lastChecked == null) ? 0 : this.lastChecked.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Link (");

        sb.append(linkId);
        sb.append(", ").append(url);
        sb.append(", ").append(lastModified);
        sb.append(", ").append(lastChecked);

        sb.append(")");
        return sb.toString();
    }
}
