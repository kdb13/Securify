package com.lina.securify.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.List;
import java.util.Objects;

/**
 * This repository holds the data about Securify inside Cloud Firestore.
 */
public class FirestoreRepository {

    private static final String TAG = FirestoreRepository.class.getSimpleName();

    private static FirestoreRepository instance;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private FirestoreRepository() { }

    public static FirestoreRepository getInstance() {
        if (instance == null)
            instance = new FirestoreRepository();

        return instance;
    }

    /**
     * Returns all the volunteers of the currently signed in user.
     * @return A Query for all volunteers
     */
    public CollectionReference getVolunteers() {

        return firestore
                .collection(Collections.USERS)
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection(Collections.VOLUNTEERS);

    }

    /**
     * Adds a new volunteer
     * @param volunteer The new volunteer to add
     */
    public void addVolunteer(Volunteer volunteer) {

        getVolunteers()
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

        getVolunteers()
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
                        Objects.requireNonNull(auth.getCurrentUser()).getUid());

    }

}
