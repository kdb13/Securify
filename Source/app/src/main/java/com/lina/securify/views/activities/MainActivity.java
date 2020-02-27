package com.lina.securify.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.R;
import com.lina.securify.data.models.Alert;
import com.lina.securify.databinding.ActivityMainBinding;
import com.lina.securify.views.dialogs.ReceiveAlertDialog;
import com.lina.securify.views.dialogs.SystemOverlayPermissionDialogFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;

    private static final String SYSTEM_OVERLAY_PERMISSION_DIALOG =
            "com.lina.securify.SYSTEM_OVERLAY_PERMISSION_DIALOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new ReceiveAlertDialog(this, new Alert("John Doe", "8000046911", "No location"));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.toolbar.setTitle(R.string.fragment_home);

        setSupportActionBar(binding.toolbar);

        setupNavigation();
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    private void checkForPlayServices() {

        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int code;

        if ((code = availability.isGooglePlayServicesAvailable(this))
                != ConnectionResult.SUCCESS) {

            availability.getErrorDialog(this, code, -1).show();

        }

    }

}
