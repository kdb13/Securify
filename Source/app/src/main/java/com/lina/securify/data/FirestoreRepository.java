package com.lina.securify.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.lina.securify.data.models.LoginCredentials;
import com.lina.securify.data.models.SignUpCredentials;
import com.lina.securify.data.models.Volunteer;
import com.lina.securify.data.repositories.AuthTaskListener;
import com.lina.securify.contracts.Collections;
import com.lina.securify.contracts.UsersContract;
import com.lina.securify.contracts.VolunteersContract;

import java.util.HashMap;
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

    private FirestoreRepository() {
    }

    public static FirestoreRepository getInstance() {
        if (instance == null)
            instance = new FirestoreRepository();

        return instance;
    }

    public void checkPhoneExists(String phone, AuthTaskListener listener) {

        auth.signInAnonymously()
                .addOnSuccessListener(authResult -> {

                    firestore.collection(Collections.USERS)
                            .whereEqualTo(UsersContract.PHONE, phone)
                            .get()
                            .addOnSuccessListener(querySnapshot -> {

                                if (!querySnapshot.isEmpty())
                                    listener.onComplete(AuthTaskListener.EXISTING_PHONE);
                                else
                                    listener.onComplete(-1);

                            })
                            .addOnFailureListener(e -> Log.e(TAG, "Error checking existing phone!", e));

                })
                .addOnFailureListener(e -> Log.e(TAG, "Error signing-in anonymously!", e));

    }

    public void checkEmailExists(String email, AuthTaskListener listener) {

        auth.fetchSignInMethodsForEmail(email)
                .addOnSuccessListener(result -> {

                    List<String> signInMethods = result.getSignInMethods();

                    if (signInMethods.isEmpty())
                        listener.onComplete(AuthTaskListener.NEW_EMAIL);
                    else
                        listener.onComplete(AuthTaskListener.EXISTING_EMAIL);

                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching Sign In methods.", e));

    }

    public void login(LoginCredentials credentials, AuthTaskListener listener) {

        auth.signInWithEmailAndPassword(credentials.getEmail(), credentials.getPassword())
                .addOnSuccessListener(
                        authResult -> listener.onComplete(AuthTaskListener.SIGNED_IN))
                .addOnFailureListener(e -> {

                    if (e instanceof FirebaseAuthInvalidCredentialsException)
                        listener.onComplete(AuthTaskListener.INCORRECT_PASSWORD);
                    else
                        Log.e(TAG, "An unknown error occurred!", e);

                });

    }

    public void verifyOtp(SignUpCredentials credentials, String verificationId, AuthTaskListener listener) {

        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                verificationId, credentials.getOtpCode()
        );

        // Verify the OTP

        auth.getCurrentUser().updatePhoneNumber(phoneAuthCredential)
                .addOnSuccessListener(authResult -> {

                    // Link email and password with account
                    auth.getCurrentUser().linkWithCredential(EmailAuthProvider.getCredential(
                            credentials.getEmail(), credentials.getPassword()))
                            .addOnSuccessListener(linkResult -> {

                                // Create the user document
                                HashMap<String, String> newUser = new HashMap<>();
                                newUser.put(UsersContract.FIRST_NAME, credentials.getFirstName());
                                newUser.put(UsersContract.LAST_NAME, credentials.getLastName());
                                newUser.put(UsersContract.PHONE, credentials.getPhone());

                                firestore.collection(Collections.USERS)
                                        .document(linkResult.getUser().getUid())
                                        .set(newUser)
                                        .addOnSuccessListener(createResult -> {

                                            listener.onComplete(AuthTaskListener.SIGNED_UP);

                                        })
                                        .addOnFailureListener(e -> Log.e(TAG, "Error creating user document!", e));

                            })
                            .addOnFailureListener(e -> Log.e(TAG, "Error linking credentials!", e));

                })
                .addOnFailureListener(e -> {

                    if (e instanceof FirebaseAuthInvalidCredentialsException)
                        listener.onComplete(AuthTaskListener.INCORRECT_OTP);
                    else
                        Log.e(TAG, "An unknown error verifying OTP!", e);

                });

    }

    /**
     * Returns all the volunteers of the currently signed in user.
     *
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
     *
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
     *
     * @param phones A list of phones
     */
    public void removeVolunteers(List<String> phones) {

        getVolunteers()
                .whereIn(VolunteersContract.PHONE, phones)
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
                .whereEqualTo(UsersContract.PHONE, phone);

    }

    public Query isExistingVolunteer(String phone) {
        return getCurrentUserDocument()
                .collection(Collections.VOLUNTEERS)
                .whereEqualTo(VolunteersContract.PHONE, phone);
    }

    public DocumentReference getCurrentUserDocument() {

        return firestore.document(
                Collections.USERS + "/" +
                        Objects.requireNonNull(auth.getCurrentUser()).getUid());

    }

}
