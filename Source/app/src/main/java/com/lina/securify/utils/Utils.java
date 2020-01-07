package com.lina.securify.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.lina.securify.viewmodels.auth.Constants;

import java.util.Objects;

public class Utils {

    public static String getTextInside(TextInputLayout inputLayout) {
        return Objects.requireNonNull(inputLayout.getEditText())
                .getText()
                .toString();
    }

    /**
     * Gets the connectivity status of Internet.
     * @param context
     * @return <code>true</code> if internet is connected, else <code>false</code>
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
               activeNetwork.isConnectedOrConnecting();
    }

}
