package edu.java.bot.services.validator;

public interface LinkValidator {
    boolean isLinkValid(String url);

    void setNextValidator(LinkValidator nextValidator);
}
