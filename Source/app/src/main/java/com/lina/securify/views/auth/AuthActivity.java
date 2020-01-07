package com.lina.securify.views.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.lina.securify.R;
import com.lina.securify.views.MainActivity;

public class AuthActivity extends AppCompatActivity {

    private static final String LOG_TAG = AuthActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (isSignedIn()) {
            //goToMainActivity();
        }
    }

    private boolean isSignedIn() {
        return FirebaseAuth.getInstance()
                .getCurrentUser() != null;
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
