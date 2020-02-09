package com.lina.securify.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.R;
import com.lina.securify.databinding.ActivityMainBinding;
import com.lina.securify.utils.constants.RequestCodes;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_KDB";

    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermissions();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.toolbar.setTitle(R.string.fragment_home);

        setSupportActionBar(binding.toolbar);

        setupNavigation();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case RequestCodes.REQUEST_PERMISSION_RECEIVE_SMS:

                if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "Permission granted!");

                }

                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

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

    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.RECEIVE_SMS},
                    RequestCodes.REQUEST_PERMISSION_RECEIVE_SMS
            );

        }

    }
}
