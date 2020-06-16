package com.lina.securify.data.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import com.lina.securify.data.contracts.UsersContract;
import com.lina.securify.data.contracts.VolunteersContract;
import com.lina.securify.data.models.Volunteer;

import java.util.Iterator;
import java.util.Objects;

public final class VolunteersRepository {

    private static VolunteersRepository instance;

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private VolunteersRepository() { }

    public static VolunteersRepository getInstance() {
        if (instance == null) {
            instance = new VolunteersRepository();
        }

        return instance;
    }

    public void add(Volunteer volunteer) {
        collectionVolunteers().add(volunteer);
    }

    public void removeAll() {

        collectionVolunteers().get()
                .addOnSuccessListener(documentSnapshots -> {

                    WriteBatch batch = firestore.batch();

                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        batch.delete(documentSnapshot.getReference());
                    }

                    batch.commit();

                });

    }

    public void remove(Iterator<String> keys) {

        WriteBatch writeBatch = firestore.batch();

        while (keys.hasNext()) {
            writeBatch.delete(collectionVolunteers().document(keys.next()));
        }

        writeBatch.commit();

    }

    public Query queryVolunteers() {
        return collectionVolunteers().orderBy(VolunteersContract.NAME);
    }

    private CollectionReference collectionVolunteers() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        return firestore
                .collection(UsersContract._COLLECTION)
                .document(Objects.requireNonNull(auth.getUid()))
                .collection(VolunteersContract._COLLECTION);
    }

}
