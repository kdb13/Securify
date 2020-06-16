package com.lina.securify.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.KeyguardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.lina.securify.R;
import com.lina.securify.databinding.ActivityAlertBinding;
import com.lina.securify.utils.Utils;

public class AlertActivity extends AppCompatActivity {

    public static final String EXTRA_ALERT_PERSON = "com.lina.securify.ALERT_PERSON";
    public static final String EXTRA_ALERT_PERSON_PHONE = "com.lina.securify.ALERT_PERSON_PHONE";
    public static final String EXTRA_ALERT_LOCATION = "com.lina.securify.ALERT_LOCATION";

    private ActivityAlertBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_alert);

        turnScreenOn();

        displayAlert();
    }

    private void turnScreenOn() {

        if (Utils.isSDK(Build.VERSION_CODES.O_MR1)) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

    }

    private void displayAlert() {

        Intent intent = getIntent();

        String alertMessage = getString(R.string.alert_person_and_help_msg,
                intent.getStringExtra(EXTRA_ALERT_PERSON));

        binding.textViewAlertMessage.setText(alertMessage);

        if (intent.getStringExtra(EXTRA_ALERT_LOCATION) != null) {
            binding.setHasLocation(true);
        } else {
            binding.setHasLocation(false);
        }
    }

    public void call(View view) {

        String phone = getIntent().getStringExtra(EXTRA_ALERT_PERSON_PHONE);
        Intent intent = callIntent(phone);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void locate(View view) {

        String location = getIntent().getStringExtra(EXTRA_ALERT_LOCATION);
        Intent intent = locateIntent(location);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public static Intent callIntent(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));

        return intent;
    }

    public static Intent locateIntent(String location) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?q=" + location));

        return intent;
    }

    private void showKeyguard() {

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);


    }
}