package com.lina.securify.utils.alert;

import android.content.Context;

import com.lina.securify.R;

import java.util.regex.Pattern;

class AlertSmsPattern {

    private Pattern pattern;

    AlertSmsPattern(Context context) {

        String header = context.getString(R.string.app_name).toUpperCase();
        String needsHelp = context.getString(R.string.needs_help);
        String currentLocation = context.getString(R.string.current_location);
        String noLocation = context.getString(R.string.no_location);

        pattern = Pattern.compile("//" + header + "\\\\\\\\\\n\\n" +
                needsHelp + ":\\s([a-z|A-Z]+\\s[a-z|A-Z]+)\\s\\((\\+91\\d{10})\\)\\n\\n" +
                currentLocation + ":\\s(\\d+.\\d+,\\d+.\\d+|" + noLocation + ")"
        );

    }

    Pattern getPattern() {
        return pattern;
    }

}
