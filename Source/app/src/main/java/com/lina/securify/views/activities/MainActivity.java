package com.lina.securify.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.R;
import com.lina.securify.alerts.showalert.AlertNotification;
import com.lina.securify.databinding.ActivityMainBinding;
import com.lina.securify.utils.Utils;
import com.lina.securify.views.activities.AuthActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        setupNavigationUi();

        createNotificationChannel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Signs out a user and opens {@link AuthActivity}.
     */
    public void logOut(MenuItem menuItem) {
        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    /**
     * Sets up the activity with {@link NavigationUI}.
     */
    private void setupNavigationUi() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(binding.drawerLayout)
                        .build();

        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Update the UI when destination changes
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            if (destination.getId() == R.id.fragment_addVolunteers) {
                binding.toolbar.setNavigationIcon(R.drawable.ic_close);
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

        });
    }

    private void createNotificationChannel() {

        if (Utils.isSDK(Build.VERSION_CODES.O)) {

            String channelAlertsName = getString(R.string.channel_alerts_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(
                    AlertNotification.ALERT_CHANNEL_ID, channelAlertsName, importance);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }

        }

    }
}
