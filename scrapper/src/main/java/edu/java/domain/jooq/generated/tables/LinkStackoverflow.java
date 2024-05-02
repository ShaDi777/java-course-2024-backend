/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.generated.tables;


import edu.java.domain.jooq.generated.Keys;
import edu.java.domain.jooq.generated.Public;
import edu.java.domain.jooq.generated.tables.Link.LinkPath;
import edu.java.domain.jooq.generated.tables.records.LinkStackoverflowRecord;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


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
public class LinkStackoverflow extends TableImpl<LinkStackoverflowRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.link_stackoverflow</code>
     */
    public static final LinkStackoverflow LINK_STACKOVERFLOW = new LinkStackoverflow();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<LinkStackoverflowRecord> getRecordType() {
        return LinkStackoverflowRecord.class;
    }

    /**
     * The column <code>public.link_stackoverflow.link_id</code>.
     */
    public final TableField<LinkStackoverflowRecord, Long> LINK_ID = createField(DSL.name("link_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.link_stackoverflow.comments_count</code>.
     */
    public final TableField<LinkStackoverflowRecord, Integer> COMMENTS_COUNT = createField(DSL.name("comments_count"), SQLDataType.INTEGER.defaultValue(DSL.field(DSL.raw("0"), SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.link_stackoverflow.answers_count</code>.
     */
    public final TableField<LinkStackoverflowRecord, Integer> ANSWERS_COUNT = createField(DSL.name("answers_count"), SQLDataType.INTEGER.defaultValue(DSL.field(DSL.raw("0"), SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>public.link_stackoverflow.is_answered</code>.
     */
    public final TableField<LinkStackoverflowRecord, Boolean> IS_ANSWERED = createField(DSL.name("is_answered"), SQLDataType.BOOLEAN.defaultValue(DSL.field(DSL.raw("false"), SQLDataType.BOOLEAN)), this, "");

    private LinkStackoverflow(Name alias, Table<LinkStackoverflowRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private LinkStackoverflow(Name alias, Table<LinkStackoverflowRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.link_stackoverflow</code> table reference
     */
    public LinkStackoverflow(String alias) {
        this(DSL.name(alias), LINK_STACKOVERFLOW);
    }

    /**
     * Create an aliased <code>public.link_stackoverflow</code> table reference
     */
    public LinkStackoverflow(Name alias) {
        this(alias, LINK_STACKOVERFLOW);
    }

    /**
     * Create a <code>public.link_stackoverflow</code> table reference
     */
    public LinkStackoverflow() {
        this(DSL.name("link_stackoverflow"), null);
    }

    public <O extends Record> LinkStackoverflow(Table<O> path, ForeignKey<O, LinkStackoverflowRecord> childPath, InverseForeignKey<O, LinkStackoverflowRecord> parentPath) {
        super(path, childPath, parentPath, LINK_STACKOVERFLOW);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class LinkStackoverflowPath extends LinkStackoverflow implements Path<LinkStackoverflowRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> LinkStackoverflowPath(Table<O> path, ForeignKey<O, LinkStackoverflowRecord> childPath, InverseForeignKey<O, LinkStackoverflowRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private LinkStackoverflowPath(Name alias, Table<LinkStackoverflowRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public LinkStackoverflowPath as(String alias) {
            return new LinkStackoverflowPath(DSL.name(alias), this);
        }

        @Override
        public LinkStackoverflowPath as(Name alias) {
            return new LinkStackoverflowPath(alias, this);
        }

        @Override
        public LinkStackoverflowPath as(Table<?> alias) {
            return new LinkStackoverflowPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @NotNull
    public UniqueKey<LinkStackoverflowRecord> getPrimaryKey() {
        return Keys.LINK_STACKOVERFLOW_PKEY;
    }

    @Override
    @NotNull
    public List<ForeignKey<LinkStackoverflowRecord, ?>> getReferences() {
        return Arrays.asList(Keys.LINK_STACKOVERFLOW__LINK_STACKOVERFLOW_LINK_ID_FKEY);
    }

    private transient LinkPath _link;

    /**
     * Get the implicit join path to the <code>public.link</code> table.
     */
    public LinkPath link() {
        if (_link == null)
            _link = new LinkPath(this, Keys.LINK_STACKOVERFLOW__LINK_STACKOVERFLOW_LINK_ID_FKEY, null);

        return _link;
    }

    @Override
    @NotNull
    public LinkStackoverflow as(String alias) {
        return new LinkStackoverflow(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public LinkStackoverflow as(Name alias) {
        return new LinkStackoverflow(alias, this);
    }

    @Override
    @NotNull
    public LinkStackoverflow as(Table<?> alias) {
        return new LinkStackoverflow(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public LinkStackoverflow rename(String name) {
        return new LinkStackoverflow(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public LinkStackoverflow rename(Name name) {
        return new LinkStackoverflow(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public LinkStackoverflow rename(Table<?> name) {
        return new LinkStackoverflow(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkStackoverflow where(Condition condition) {
        return new LinkStackoverflow(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkStackoverflow where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkStackoverflow where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkStackoverflow where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public LinkStackoverflow where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public LinkStackoverflow where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public LinkStackoverflow where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public LinkStackoverflow where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkStackoverflow whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkStackoverflow whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
