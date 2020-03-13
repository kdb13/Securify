package com.lina.securify.sendalert;

import android.content.Context;
import android.util.Log;

import com.lina.securify.R;
import com.lina.securify.data.models.Alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlertSmsParser {

    private Pattern alertPattern;
    private Context context;

    public AlertSmsParser(Context context) {
        this.context = context;
        buildPattern();
    }

    public Alert parse(String sms) {

        Matcher matcher = alertPattern.matcher(sms);

        // Check if the SMS is of correct format
        if (matcher.find()) {

            Alert alert = new Alert();
            alert.setVictimName(matcher.group(1));
            alert.setVictimPhone(matcher.group(2));

            if (matcher.group(3).equals(context.getString(R.string.alert_no_location))) {
                alert.setLocation(matcher.group(3));
            } else {
                alert.setLocation(matcher.group(4));
            }

            return alert;

        }

        return null;
    }

    private void buildPattern() {
        String header = context.getString(R.string.app_name).toUpperCase();
        String needsHelp = context.getString(R.string.alert_help_message);
        String currentLocation = context.getString(R.string.alert_location_message);
        String noLocation = context.getString(R.string.alert_no_location);

        alertPattern = Pattern.compile("//" + header + "\\\\\\\\\\n\\n" +
                needsHelp + ":\\s([a-z|A-Z]+\\s[a-z|A-Z]+)\\s\\((\\d{10})\\)\\n\\n" +
                currentLocation +
                ":\\s(https://www.google.com/maps/search/\\?api=1&query=([0-9-]+.\\d+,[0-9-]+.\\d+)|"
                + noLocation + ")"
        );
    }
}
