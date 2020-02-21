package com.lina.securify.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.lina.securify.R;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.models.Alert;
import com.lina.securify.data.models.NewUser;
import com.lina.securify.utils.constants.MetaUser;
import com.lina.securify.utils.constants.MetaVolunteer;

import java.text.Format;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to build the Alert SMS and also parse the Alert back from the
 * received SMS.
 */
public class AlertSmsBuilder {

    private static final String TAG = AlertSmsBuilder.class.getSimpleName();

    private Pattern ALERT_SMS_PATTERN;
    private Context context;

    private String header;
    private String needsHelp;
    private String currentLocation;

    public AlertSmsBuilder(Context context) {

        this.context = context;

        header = context.getString(R.string.app_name).toUpperCase();
        needsHelp = context.getString(R.string.needs_help);
        currentLocation = context.getString(R.string.current_location);

        ALERT_SMS_PATTERN = Pattern.compile(
                "//" + header + "\\\\\\\\\\n\\n" +
                needsHelp + ":\\s[a-z|A-Z]+\\s[a-z|A-Z]+\\s\\(\\+91\\d{10}\\)\\n\\n" +
                currentLocation + ":\\s\\d+.\\d+:\\d+.\\d+");

    }

    /**
     * Builds the SMS from an Alert object.
     *
     * @param alert The Alert used to build the SMS.
     * @return Built SMS
     */
    public String buildSms(@NonNull Alert alert) {

        if (alert.getLocation() == null ||
            alert.getVictimPhone() == null ||
            alert.getVictimName() == null)
            return null;

        // Build the SMS
        String alertInfo = context.getString(
                R.string.alert_info,
                needsHelp,
                alert.getVictimName(),
                alert.getVictimPhone());

        String alertLocation = context.getString(
                R.string.alert_location,
                currentLocation,
                alert.getLocation());

        return context.getString(
                R.string.alert_sms,
                header,
                alertInfo,
                alertLocation);
    }

    /**
     * Parses the received SMS and builds the Alert object from it.
     *
     * @param sms The received SMS
     * @return New Alert object or null if SMS is invalid
     */
    public Alert parseAlertFromSms(@NonNull String sms) {

        Matcher matcher = ALERT_SMS_PATTERN.matcher(sms);

        // Check if the SMS is of correct format
        if (matcher.find()) {

            Alert alert = new Alert();
            AlertPatterns patterns = new AlertPatterns();

            matcher = patterns.INFO.matcher(sms);

            if (matcher.find()) {

                // Get the victim's name
                int index = matcher.end();
                matcher = patterns.NAME.matcher(sms);

                if (matcher.find(index)) {

                    alert.setVictimName(
                            sms.substring(matcher.start(), matcher.end())
                    );

                }

                // Get the victim's phone number
                index = matcher.end() + 1;
                matcher = patterns.PHONE.matcher(sms);

                if (matcher.find(index)) {

                    alert.setVictimPhone(
                            sms.substring(matcher.start() + 1, matcher.end() - 1)
                    );

                }

                // Get the victim's location
                matcher = Pattern.compile(currentLocation + ":\\s").matcher(sms);

                if (matcher.find()) {

                    index = matcher.end();

                    matcher = patterns.LOCATION.matcher(sms);

                    if (matcher.find(index)) {

                        alert.setLocation(
                                sms.substring(matcher.start(), matcher.end())
                        );

                    }
                }
            }

            return alert;
        }

        return null;
    }

    private class AlertPatterns {
        final Pattern INFO = Pattern.compile(
                "//" + header + "\\\\\\\\\\n\\n" +
                needsHelp + ":\\s");

        final Pattern NAME = Pattern.compile("[a-z|A-Z]+\\s[a-z|A-Z]+");
        final Pattern PHONE = Pattern.compile("\\(\\+91\\d{10}\\)");
        final Pattern LOCATION = Pattern.compile("\\d+.\\d+:\\d+.\\d+");
    }
}
