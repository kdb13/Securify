package com.lina.securify.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lina.securify.R;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // Go to MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        // Delete the anonymous user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            if (user.getEmail() == null) {
                FirebaseAuth.getInstance().getCurrentUser().delete()
                        .addOnFailureListener(e -> Log.e("KDB", "Error: ", e));
            }

        }
    }
}
