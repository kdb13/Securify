package com.lina.securify.utils.alert;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lina.securify.R;
import com.lina.securify.data.models.Alert;

import java.util.regex.Matcher;

/**
 * This class is used to build the Alert SMS and also parse the Alert back from the
 * received SMS.
 */
public class AlertSmsBuilder {

    private static final String TAG = AlertSmsBuilder.class.getSimpleName();
    private static final String MAPS_URL = "https://www.google.com/maps/search/?api=1&query=";

    private Context context;
    private AlertSmsPattern alertSmsPattern;

    public AlertSmsBuilder(Context context) {

        this.context = context;
        alertSmsPattern = new AlertSmsPattern(context);

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
                context.getString(R.string.needs_help),
                alert.getVictimName(),
                alert.getVictimPhone());

        // Set the location
        String _location;

        if (alert.getLocation().equals(context.getString(R.string.no_location)))
            _location = alert.getLocation();
        else
            _location = MAPS_URL + alert.getLocation();

        String alertLocation = context.getString(
                R.string.alert_location,
                context.getString(R.string.current_location),
                _location);

        return context.getString(
                R.string.alert_sms,
                context.getString(R.string.app_name).toUpperCase(),
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

        Matcher matcher = alertSmsPattern.getPattern().matcher(sms);

        // Check if the SMS is of correct format
        if (matcher.find()) {

            Alert alert = new Alert();
            alert.setVictimName(matcher.group(1));
            alert.setVictimPhone(matcher.group(2));

            if (matcher.group(3).equals(context.getString(R.string.no_location))) {
                alert.setLocation(matcher.group(3));
            } else {
                alert.setLocation(matcher.group(4));
            }

            return alert;

        }

        return null;
    }

}
