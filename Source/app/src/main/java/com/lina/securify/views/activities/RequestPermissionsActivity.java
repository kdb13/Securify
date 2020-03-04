package com.lina.securify.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.lina.securify.R;
import com.lina.securify.utils.Utils;

public class RequestPermissionsActivity extends AppCompatActivity {

    private static final int REQUEST_GRANT_PERMISSIONS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_GRANT_PERMISSIONS) {

            if (grantResults.length > 0) {

                for (int result : grantResults) {

                    if (result == PackageManager.PERMISSION_DENIED) {
                        return;
                    }

                }

                startActivity(new Intent(this, MainActivity.class));
                finish();

            }

        }

    }

    public void onExitClick(View view) {
        finish();
    }

    public void onGrantClick(View view) {
        ActivityCompat.requestPermissions(this,
                Utils.appPermissions, REQUEST_GRANT_PERMISSIONS);
    }
}
