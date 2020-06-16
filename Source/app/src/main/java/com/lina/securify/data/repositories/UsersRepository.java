package com.lina.securify.data.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lina.securify.data.contracts.UsersContract;

import java.util.Objects;

public class UsersRepository {

    private static UsersRepository instance;

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private UsersRepository() { }

    public static UsersRepository getInstance() {
        if (instance == null) {
            instance = new UsersRepository();
        }

        return instance;
    }

    public DocumentReference getCurrentUser() {
        return firestore.collection(UsersContract._COLLECTION)
                .document(
                        Objects.requireNonNull(auth.getCurrentUser()).getUid()
                );
    }
}
