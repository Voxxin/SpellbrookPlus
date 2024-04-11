package com.github.voxxin.spellbrookplus.core.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisplayName {

    public static String ignFromDisplayName(String content) {
        Pattern pattern = Pattern.compile("[\\w]+");
        Matcher matcher = pattern.matcher(content);
        String username = matcher.find() ? matcher.group(0) : null;
        if (username == null || username.isEmpty()) {
            return content;
        } else return username;
    }
}
