package com.lina.securify.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.lina.securify.data.models.Volunteer;
import com.lina.securify.utils.constants.Collections;
import com.lina.securify.utils.constants.MetaUser;
import com.lina.securify.utils.constants.MetaVolunteer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This repository holds the data about Securify inside Cloud Firestore.
 */
public class FirestoreRepository {

    private static final String TAG = FirestoreRepository.class.getSimpleName();

    private static FirestoreRepository instance;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    private FirestoreRepository() {
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirestoreRepository getInstance() {
        if (instance == null)
            instance = new FirestoreRepository();

        return instance;
    }

    /**
     * Returns all the volunteers of the currently signed in user.
     * @return A Query for all volunteers
     */
    public Query getVolunteers() {

        return volunteersRef();

    }

    /**
     * Adds a new volunteer
     * @param volunteer The new volunteer to add
     */
    public void addVolunteer(Volunteer volunteer) {

        volunteersRef()
                .add(volunteer)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding volunteer!", e);
                    }
                });

    }

    /**
     * Removes the volunteers with the specified phone numbers.
     * @param phones A list of phones
     */
    public void removeVolunteers(List<String> phones) {

        volunteersRef()
                .whereIn(MetaVolunteer.PHONE, phones)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        // Run a batched write to delete the volunteers
                        WriteBatch batch = firestore.batch();

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots)
                            batch.delete(snapshot.getReference());

                        batch.commit().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error removing volunteers!", e);
                            }
                        });
                    }

                });

    }

    public Query userExistsWithPhone(String phone) {

        return firestore
                .collection(Collections.USERS)
                .whereEqualTo(MetaUser.PHONE, phone);

    }

    public DocumentReference getCurrentUserDocument() {

        return firestore.document(
                Collections.USERS + "/" +
                        Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());

    }

    private CollectionReference volunteersRef() {

        return firestore
                .collection(Collections.USERS)
                .document(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .collection(Collections.VOLUNTEERS);

    }
}
