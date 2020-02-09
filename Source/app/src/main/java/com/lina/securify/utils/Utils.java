package com.lina.securify.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.R;
import com.lina.securify.data.models.Alert;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String getTextInside(TextInputLayout inputLayout) {
        return Objects.requireNonNull(inputLayout.getEditText())
                .getText()
                .toString();
    }

    /**
     * Gets the connectivity status of Internet.
     * @return <code>true</code> if internet is connected, else <code>false</code>
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
               activeNetwork.isConnectedOrConnecting();

    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void changeStatusBarColor(Activity activity, int color) {

        activity.getWindow()
                .setStatusBarColor(ContextCompat.getColor(activity, color));

    }

    public static String makeAlertMessage(Context context, Alert alert) {

        return context.getString(R.string.alert_message,
                alert.getVictimName(),
                alert.getVictimPhone(),
                alert.getLocation());

    }

    public static boolean isAnAlert(String messageBody) {

        return
                Pattern
                        .compile("//SECURIFY\\\\")
                        .matcher(messageBody)
                        .find();

    }

    public static String trimPhone(String phone) {

        StringBuilder string = new StringBuilder(phone);

        int index;

        while ( (index = string.indexOf(" ")) != -1 )
            string.delete(index, index + 1);

        if (string.length() > 10) {

            if ( (index = string.indexOf("+91")) == 0 ) {

                string.delete(index, index + 3);

            } else if ( (index = string.indexOf("91")) == 0 ) {

                string.delete(index, index + 2);

            }
        }

        return string.toString();
    }

}
