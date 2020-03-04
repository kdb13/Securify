package com.lina.securify.views.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import com.lina.securify.R;
import com.lina.securify.databinding.ActivityPermissionsBinding;
import com.lina.securify.utils.AccessibilityUtils;
import com.lina.securify.utils.Utils;

public class PermissionsActivity extends AppCompatActivity {

    private ActivityPermissionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_permissions);

        setToolbar();

        if (Utils.isM()) {
            binding.checkSystemOverlay.setChecked(true);
            binding.checkSystemOverlay.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Utils.canDrawOverlays(this) && AccessibilityUtils.isEnabled(this)) {
            finish();
            return;
        }

        binding.checkSystemOverlay.setChecked(Utils.canDrawOverlays(this));
        binding.checkAccessibility.setChecked(AccessibilityUtils.isEnabled(this));
    }

    public void goToAccessibilitySettings(View view) {
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void goToSystemOverlaySettings(View view) {

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));

        startActivity(intent);

    }

    private void setToolbar() {
        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
