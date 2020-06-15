package com.lina.securify.alerts.showalert;

import android.content.Context;

import com.lina.securify.R;
import com.lina.securify.alerts.Victim;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlertSmsParser {

    private Pattern alertPattern;
    private Context context;

    public AlertSmsParser(Context context) {
        this.context = context;
        buildPattern();
    }

    public Victim parse(String sms) {

        Matcher matcher = alertPattern.matcher(sms);

        // Check if the SMS is of correct format
        if (matcher.find()) {

            Victim victim = new Victim();
            victim.setName(matcher.group(1));
            victim.setPhone(matcher.group(2));

            if (matcher.group(3).equals(context.getString(R.string.alert_no_location))) {
                victim.setLocation(null);
            } else {
                victim.setLocation(matcher.group(4));
            }

            return victim;

        }

        return null;
    }

    private void buildPattern() {
        String appName = context.getString(R.string.app_name).toUpperCase();
        String alertHelpMessage = context.getString(R.string.alert_help_message);
        String alertCurrentLocation = context.getString(R.string.alert_current_location);
        String alertNoLocation = context.getString(R.string.alert_no_location);

        alertPattern = Pattern.compile("//" + appName + "\\\\\\\\\\n\\n" +
                alertHelpMessage + ":\\s([a-z|A-Z]+\\s[a-z|A-Z]+)\\s\\((\\d{10})\\)\\n\\n" +
                alertCurrentLocation +
                ":\\s(https://www.google.com/maps/search/\\?api=1&query=([0-9-]+.\\d+,[0-9-]+.\\d+)|"
                + alertNoLocation + ")"
        );
    }

}
