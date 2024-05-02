package edu.java.utils;

import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkUtils {
    private static final String PROTOCOL_SEPARATOR = "://";
    private static final String PATH_SEPARATOR = "/";

    public static String getDomainName(String url) {
        int indexProtocolSeparator = url.indexOf(PROTOCOL_SEPARATOR);
        if (indexProtocolSeparator == -1) {
            indexProtocolSeparator = 0;
        } else {
            indexProtocolSeparator += PROTOCOL_SEPARATOR.length();
        }

        int indexFirstPathSeparator = url.indexOf(PATH_SEPARATOR, indexProtocolSeparator);
        if (indexFirstPathSeparator == -1) {
            indexFirstPathSeparator = url.length();
        }

        return url.substring(indexProtocolSeparator, indexFirstPathSeparator);
    }

    public static List<String> getExtensions(String url) {
        int skipParts = 1;
        String copyUrl = url.replace(PROTOCOL_SEPARATOR, PATH_SEPARATOR);
        if (!copyUrl.equals(url)) {
            skipParts++;
        }

        String[] urlParts = copyUrl.split(PATH_SEPARATOR);
        return List.of(Arrays.copyOfRange(urlParts, skipParts, urlParts.length));
    }
}
