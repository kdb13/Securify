package com.lina.securify.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lina.securify.R;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

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
