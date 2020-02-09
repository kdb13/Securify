package com.lina.securify.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lina.securify.R;
import com.lina.securify.views.dialogs.HelpAlertDialogBuilder;
import com.lina.securify.views.dialogs.ReceiveAlertDialog;

public class AuthActivity extends AppCompatActivity {

    private static final String LOG_TAG = AuthActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        new ReceiveAlertDialog(this);
    }
}
