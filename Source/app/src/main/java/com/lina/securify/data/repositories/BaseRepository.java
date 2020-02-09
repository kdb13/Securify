package com.lina.securify.data.repositories;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

public class BaseRepository {

    protected FirebaseAuth firebaseAuth;

    protected BaseRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    protected String getCurrentUserID() {
        if (firebaseAuth.getCurrentUser() == null)
            return null;
        else
            return firebaseAuth.getCurrentUser().getUid();
    }
}
