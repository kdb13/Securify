package com.lina.securify.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.R;
import com.lina.securify.views.auth.AuthActivity;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
