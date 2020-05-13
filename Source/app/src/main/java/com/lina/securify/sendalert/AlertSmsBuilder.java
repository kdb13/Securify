package com.lina.securify.sendalert;

import android.content.Context;
import android.text.Html;

import com.lina.securify.R;
import com.lina.securify.data.models.Alert;

class AlertSmsBuilder {

    private AlertData alertData;
    private Context context;

    AlertSmsBuilder(Context context, AlertData alertData) {
        this.alertData = alertData;
        this.context = context;
    }

    String build() {

        String header = context.getString(R.string.alert_header,
                context.getString(R.string.app_name).toUpperCase());

        String locationContent = context.getString(R.string.alert_no_location);

        if (!alertData.getLocation().equals(locationContent))
            locationContent = context.getString(R.string.google_maps_url) + alertData.getLocation();

        String location = context.getString(R.string.alert_location,
                context.getString(R.string.alert_location_message),
                locationContent);

        String description = context.getString(R.string.alert_description,
                context.getString(R.string.alert_help_message),
                alertData.getVictimName(), alertData.getVictimPhone());

        return context.getString(R.string.alert_sms,
                header, description, location);

    }
}
