package com.lina.securify.data.repositories;

import android.util.Log;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lina.securify.data.contracts.UsersContract;
import com.lina.securify.data.models.LoginCredentials;
import com.lina.securify.data.models.SignUpCredentials;

import java.util.HashMap;
import java.util.List;

/**
 * This repository holds the data about Securify inside Cloud Firestore.
 */
public class AuthRepository {

    private static final String TAG = AuthRepository.class.getSimpleName();

    private static AuthRepository instance;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private AuthRepository() {
    }

    public static AuthRepository getInstance() {
        if (instance == null)
            instance = new AuthRepository();

        return instance;
    }

    public void checkPhoneExists(String phone, AuthTaskListener listener) {

        auth.signInAnonymously()
                .addOnSuccessListener(authResult -> {

                    firestore.collection(UsersContract._COLLECTION)
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

                                firestore.collection(UsersContract._COLLECTION)
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

}
