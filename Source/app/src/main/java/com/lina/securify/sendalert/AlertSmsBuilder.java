package com.lina.securify.sendalert;

import android.content.Context;
import android.text.Html;

import com.lina.securify.R;
import com.lina.securify.data.models.Alert;

public class AlertSmsBuilder {

    private Alert alert;
    private Context context;

    public AlertSmsBuilder(Context context, Alert alert) {
        this.alert = alert;
        this.context = context;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public String build() {

        if (alert == null)
            throw new NullPointerException("The specified Alert object is null!");

        String header = context.getString(R.string.alert_header,
                context.getString(R.string.app_name).toUpperCase());

        String locationContent = context.getString(R.string.alert_no_location);

        if (!alert.getLocation().equals(locationContent))
            locationContent = context.getString(R.string.google_maps_url) + alert.getLocation();

        String location = context.getString(R.string.alert_location,
                context.getString(R.string.alert_location_message),
                locationContent);

        String description = context.getString(R.string.alert_description,
                context.getString(R.string.alert_help_message),
                alert.getVictimName(), alert.getVictimPhone());

        return context.getString(R.string.alert_sms,
                header, description, location);

    }
}
