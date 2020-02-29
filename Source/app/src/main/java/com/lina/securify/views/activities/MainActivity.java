package com.lina.securify.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.UiAutomation;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.R;
import com.lina.securify.data.models.Alert;
import com.lina.securify.databinding.ActivityMainBinding;
import com.lina.securify.views.dialogs.ReceiveAlertDialog;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.toolbar.setTitle(R.string.fragment_home);

        setSupportActionBar(binding.toolbar);
        new ReceiveAlertDialog(this, new Alert("John", "567", "No location"));
        // Setup the NavigationUI
        setupNavigationUi();

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

    private void setupNavigationUi() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host);

        // Used to manage the Navigation button
        AppBarConfiguration appBarConfiguration =
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
