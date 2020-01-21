package com.lina.securify.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

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
}
