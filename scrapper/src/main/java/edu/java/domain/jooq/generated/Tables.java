/*
 * This file is generated by jOOQ.
 */
package edu.java.domain.jooq.generated;


import edu.java.domain.jooq.generated.tables.Chat;
import edu.java.domain.jooq.generated.tables.Databasechangelog;
import edu.java.domain.jooq.generated.tables.Databasechangeloglock;
import edu.java.domain.jooq.generated.tables.Link;
import edu.java.domain.jooq.generated.tables.LinkChat;
import edu.java.domain.jooq.generated.tables.LinkStackoverflow;

import javax.annotation.processing.Generated;


/**
 * Convenience access to all tables in public.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.19.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Tables {

    /**
     * The table <code>public.chat</code>.
     */
    public static final Chat CHAT = Chat.CHAT;

    /**
     * The table <code>public.databasechangelog</code>.
     */
    public static final Databasechangelog DATABASECHANGELOG = Databasechangelog.DATABASECHANGELOG;

    /**
     * The table <code>public.databasechangeloglock</code>.
     */
    public static final Databasechangeloglock DATABASECHANGELOGLOCK = Databasechangeloglock.DATABASECHANGELOGLOCK;

    /**
     * The table <code>public.link</code>.
     */
    public static final Link LINK = Link.LINK;

    /**
     * The table <code>public.link_chat</code>.
     */
    public static final LinkChat LINK_CHAT = LinkChat.LINK_CHAT;

    /**
     * The table <code>public.link_stackoverflow</code>.
     */
    public static final LinkStackoverflow LINK_STACKOVERFLOW = LinkStackoverflow.LINK_STACKOVERFLOW;
}
