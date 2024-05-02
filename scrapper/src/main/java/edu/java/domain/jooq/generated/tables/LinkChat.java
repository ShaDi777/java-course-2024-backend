/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.generated.tables;


import edu.java.domain.jooq.generated.Keys;
import edu.java.domain.jooq.generated.Public;
import edu.java.domain.jooq.generated.tables.Chat.ChatPath;
import edu.java.domain.jooq.generated.tables.Link.LinkPath;
import edu.java.domain.jooq.generated.tables.records.LinkChatRecord;

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
public class LinkChat extends TableImpl<LinkChatRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.link_chat</code>
     */
    public static final LinkChat LINK_CHAT = new LinkChat();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<LinkChatRecord> getRecordType() {
        return LinkChatRecord.class;
    }

    /**
     * The column <code>public.link_chat.link_id</code>.
     */
    public final TableField<LinkChatRecord, Long> LINK_ID = createField(DSL.name("link_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.link_chat.chat_id</code>.
     */
    public final TableField<LinkChatRecord, Long> CHAT_ID = createField(DSL.name("chat_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private LinkChat(Name alias, Table<LinkChatRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private LinkChat(Name alias, Table<LinkChatRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.link_chat</code> table reference
     */
    public LinkChat(String alias) {
        this(DSL.name(alias), LINK_CHAT);
    }

    /**
     * Create an aliased <code>public.link_chat</code> table reference
     */
    public LinkChat(Name alias) {
        this(alias, LINK_CHAT);
    }

    /**
     * Create a <code>public.link_chat</code> table reference
     */
    public LinkChat() {
        this(DSL.name("link_chat"), null);
    }

    public <O extends Record> LinkChat(Table<O> path, ForeignKey<O, LinkChatRecord> childPath, InverseForeignKey<O, LinkChatRecord> parentPath) {
        super(path, childPath, parentPath, LINK_CHAT);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class LinkChatPath extends LinkChat implements Path<LinkChatRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> LinkChatPath(Table<O> path, ForeignKey<O, LinkChatRecord> childPath, InverseForeignKey<O, LinkChatRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private LinkChatPath(Name alias, Table<LinkChatRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public LinkChatPath as(String alias) {
            return new LinkChatPath(DSL.name(alias), this);
        }

        @Override
        public LinkChatPath as(Name alias) {
            return new LinkChatPath(alias, this);
        }

        @Override
        public LinkChatPath as(Table<?> alias) {
            return new LinkChatPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    @NotNull
    public UniqueKey<LinkChatRecord> getPrimaryKey() {
        return Keys.LINK_CHAT_PKEY;
    }

    @Override
    @NotNull
    public List<ForeignKey<LinkChatRecord, ?>> getReferences() {
        return Arrays.asList(Keys.LINK_CHAT__LINK_CHAT_LINK_ID_FKEY, Keys.LINK_CHAT__LINK_CHAT_CHAT_ID_FKEY);
    }

    private transient LinkPath _link;

    /**
     * Get the implicit join path to the <code>public.link</code> table.
     */
    public LinkPath link() {
        if (_link == null)
            _link = new LinkPath(this, Keys.LINK_CHAT__LINK_CHAT_LINK_ID_FKEY, null);

        return _link;
    }

    private transient ChatPath _chat;

    /**
     * Get the implicit join path to the <code>public.chat</code> table.
     */
    public ChatPath chat() {
        if (_chat == null)
            _chat = new ChatPath(this, Keys.LINK_CHAT__LINK_CHAT_CHAT_ID_FKEY, null);

        return _chat;
    }

    @Override
    @NotNull
    public LinkChat as(String alias) {
        return new LinkChat(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public LinkChat as(Name alias) {
        return new LinkChat(alias, this);
    }

    @Override
    @NotNull
    public LinkChat as(Table<?> alias) {
        return new LinkChat(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public LinkChat rename(String name) {
        return new LinkChat(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public LinkChat rename(Name name) {
        return new LinkChat(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public LinkChat rename(Table<?> name) {
        return new LinkChat(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkChat where(Condition condition) {
        return new LinkChat(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkChat where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkChat where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkChat where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public LinkChat where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public LinkChat where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public LinkChat where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    @PlainSQL
    public LinkChat where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkChat whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @NotNull
    public LinkChat whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
