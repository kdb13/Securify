package com.lina.securify.views.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.R;
import com.lina.securify.data.models.Alert;
import com.lina.securify.databinding.ActivityMainBinding;
import com.lina.securify.utils.constants.RequestCodes;
import com.lina.securify.views.dialogs.ReceiveAlertDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;

    private static final String SYSTEM_OVERLAY_PERMISSION_DIALOG =
            "com.lina.securify.SYSTEM_OVERLAY_PERMISSION_DIALOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.toolbar.setTitle(R.string.fragment_home);

        setSupportActionBar(binding.toolbar);

        setupNavigation();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkForSystemOverlayPermission();

        checkForPlayServices();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host);

        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logOut(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.item_log_out) {

            FirebaseAuth
                    .getInstance()
                    .signOut();

            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);

            finish();
        }

    }

    private void setupNavigation() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host);

        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(binding.drawerLayout)
                        .build();

        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void checkForSystemOverlayPermission() {

        if (!Settings.canDrawOverlays(this)) {

            // TODO: Find the solution to app not getting notified about permission grant
            new SystemOverlayPermissionDialogFragment(this).show(getSupportFragmentManager(),
                    SYSTEM_OVERLAY_PERMISSION_DIALOG);

        }

    }

    private void checkForPlayServices() {

        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int code;

        if ((code = availability.isGooglePlayServicesAvailable(this))
                != ConnectionResult.SUCCESS) {

            availability.getErrorDialog(this, code, -1).show();

        }

    }

    public static class SystemOverlayPermissionDialogFragment extends DialogFragment {

        private Context context;

        public SystemOverlayPermissionDialogFragment(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage(R.string.display_over_other_apps_permission_message);
            builder.setNegativeButton(R.string.button_cancel, null);
            builder.setPositiveButton(R.string.button_grant, (dialog, which) -> {

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + context.getPackageName()));

                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }

                dismiss();

            });

            return builder.create();
        }

    }
}
