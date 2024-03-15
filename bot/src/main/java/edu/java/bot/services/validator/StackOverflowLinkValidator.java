package edu.java.bot.services.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackOverflowLinkValidator implements LinkValidator {
    private static final Pattern PATTERN = Pattern.compile("https?://stackoverflow.com/questions/\\d+/.+", Pattern.CASE_INSENSITIVE);
    private LinkValidator nextValidator;

    @Override
    public boolean isLinkValid(String url) {
        Matcher matcher = PATTERN.matcher(url);
        if (matcher.matches()) {
            return true;
        }

        return nextValidator != null && nextValidator.isLinkValid(url);
    }

    @Override
    public void setNextValidator(LinkValidator nextValidator) {
        this.nextValidator = nextValidator;
    }
}
