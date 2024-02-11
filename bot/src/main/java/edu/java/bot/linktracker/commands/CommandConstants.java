package edu.java.bot.linktracker.commands;

public final class CommandConstants {
    public static final String LANGUAGE_CODE = "RU";

    public static final String START_COMMAND = "/start";
    public static final String HELP_COMMAND = "/help";
    public static final String TRACK_COMMAND = "/track";
    public static final String UNTRACK_COMMAND = "/untrack";
    public static final String LIST_COMMAND = "/list";

    public static final String START_DESCRIPTION = "зарегистрироваться";
    public static final String HELP_DESCRIPTION = "вывести список доступных команд";
    public static final String TRACK_DESCRIPTION = "начать отслеживать ресурс";
    public static final String UNTRACK_DESCRIPTION = "перестать отслеживать ресурс";
    public static final String LIST_DESCRIPTION = "список отслеживаемых ресурсов";

    public static final String START_REPLY_TEXT = "Вы зарегистрированы";
    public static final String TRACK_REPLY_TEXT = "Какой ресурс начать отслеживать?";
    public static final String UNTRACK_REPLY_TEXT = "Какой ресурс перестать отслеживать?";
    public static final String LIST_EMPTY_REPLY_TEXT = "Вы ничего не отслеживаете";
    public static final String LIST_FILLED_REPLY_TEXT = "Список отслеживаемых ресурсов:";

    public static final String UNSUPPORTED_TEXT = "Неподдерживаемая команда";

    private CommandConstants() {
    }
}
