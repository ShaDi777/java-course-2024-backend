/*
 * This file is generated by jOOQ.
 */
package edu.java.dao.jooq.generated.tables.records;


import edu.java.dao.jooq.generated.tables.Databasechangelog;

import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.impl.TableRecordImpl;


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
public class DatabasechangelogRecord extends TableRecordImpl<DatabasechangelogRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.databasechangelog.id</code>.
     */
    public void setId(@NotNull String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.databasechangelog.id</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 255)
    @NotNull
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.databasechangelog.author</code>.
     */
    public void setAuthor(@NotNull String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.databasechangelog.author</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 255)
    @NotNull
    public String getAuthor() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.databasechangelog.filename</code>.
     */
    public void setFilename(@NotNull String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.databasechangelog.filename</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 255)
    @NotNull
    public String getFilename() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.databasechangelog.dateexecuted</code>.
     */
    public void setDateexecuted(@NotNull LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.databasechangelog.dateexecuted</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public LocalDateTime getDateexecuted() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>public.databasechangelog.orderexecuted</code>.
     */
    public void setOrderexecuted(@NotNull Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.databasechangelog.orderexecuted</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Integer getOrderexecuted() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>public.databasechangelog.exectype</code>.
     */
    public void setExectype(@NotNull String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.databasechangelog.exectype</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 10)
    @NotNull
    public String getExectype() {
        return (String) get(5);
    }

    /**
     * Setter for <code>public.databasechangelog.md5sum</code>.
     */
    public void setMd5sum(@Nullable String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.databasechangelog.md5sum</code>.
     */
    @Size(max = 35)
    @Nullable
    public String getMd5sum() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.databasechangelog.description</code>.
     */
    public void setDescription(@Nullable String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.databasechangelog.description</code>.
     */
    @Size(max = 255)
    @Nullable
    public String getDescription() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.databasechangelog.comments</code>.
     */
    public void setComments(@Nullable String value) {
        set(8, value);
    }

    /**
     * Getter for <code>public.databasechangelog.comments</code>.
     */
    @Size(max = 255)
    @Nullable
    public String getComments() {
        return (String) get(8);
    }

    /**
     * Setter for <code>public.databasechangelog.tag</code>.
     */
    public void setTag(@Nullable String value) {
        set(9, value);
    }

    /**
     * Getter for <code>public.databasechangelog.tag</code>.
     */
    @Size(max = 255)
    @Nullable
    public String getTag() {
        return (String) get(9);
    }

    /**
     * Setter for <code>public.databasechangelog.liquibase</code>.
     */
    public void setLiquibase(@Nullable String value) {
        set(10, value);
    }

    /**
     * Getter for <code>public.databasechangelog.liquibase</code>.
     */
    @Size(max = 20)
    @Nullable
    public String getLiquibase() {
        return (String) get(10);
    }

    /**
     * Setter for <code>public.databasechangelog.contexts</code>.
     */
    public void setContexts(@Nullable String value) {
        set(11, value);
    }

    /**
     * Getter for <code>public.databasechangelog.contexts</code>.
     */
    @Size(max = 255)
    @Nullable
    public String getContexts() {
        return (String) get(11);
    }

    /**
     * Setter for <code>public.databasechangelog.labels</code>.
     */
    public void setLabels(@Nullable String value) {
        set(12, value);
    }

    /**
     * Getter for <code>public.databasechangelog.labels</code>.
     */
    @Size(max = 255)
    @Nullable
    public String getLabels() {
        return (String) get(12);
    }

    /**
     * Setter for <code>public.databasechangelog.deployment_id</code>.
     */
    public void setDeploymentId(@Nullable String value) {
        set(13, value);
    }

    /**
     * Getter for <code>public.databasechangelog.deployment_id</code>.
     */
    @Size(max = 10)
    @Nullable
    public String getDeploymentId() {
        return (String) get(13);
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DatabasechangelogRecord
     */
    public DatabasechangelogRecord() {
        super(Databasechangelog.DATABASECHANGELOG);
    }

    /**
     * Create a detached, initialised DatabasechangelogRecord
     */
    @ConstructorProperties({ "id", "author", "filename", "dateexecuted", "orderexecuted", "exectype", "md5sum", "description", "comments", "tag", "liquibase", "contexts", "labels", "deploymentId" })
    public DatabasechangelogRecord(@NotNull String id, @NotNull String author, @NotNull String filename, @NotNull LocalDateTime dateexecuted, @NotNull Integer orderexecuted, @NotNull String exectype, @Nullable String md5sum, @Nullable String description, @Nullable String comments, @Nullable String tag, @Nullable String liquibase, @Nullable String contexts, @Nullable String labels, @Nullable String deploymentId) {
        super(Databasechangelog.DATABASECHANGELOG);

        setId(id);
        setAuthor(author);
        setFilename(filename);
        setDateexecuted(dateexecuted);
        setOrderexecuted(orderexecuted);
        setExectype(exectype);
        setMd5sum(md5sum);
        setDescription(description);
        setComments(comments);
        setTag(tag);
        setLiquibase(liquibase);
        setContexts(contexts);
        setLabels(labels);
        setDeploymentId(deploymentId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised DatabasechangelogRecord
     */
    public DatabasechangelogRecord(edu.java.dao.jooq.generated.tables.pojos.Databasechangelog value) {
        super(Databasechangelog.DATABASECHANGELOG);

        if (value != null) {
            setId(value.getId());
            setAuthor(value.getAuthor());
            setFilename(value.getFilename());
            setDateexecuted(value.getDateexecuted());
            setOrderexecuted(value.getOrderexecuted());
            setExectype(value.getExectype());
            setMd5sum(value.getMd5sum());
            setDescription(value.getDescription());
            setComments(value.getComments());
            setTag(value.getTag());
            setLiquibase(value.getLiquibase());
            setContexts(value.getContexts());
            setLabels(value.getLabels());
            setDeploymentId(value.getDeploymentId());
            resetChangedOnNotNull();
        }
    }
}
