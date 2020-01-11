package com.lina.securify.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lina.securify.data.meta.Collections;
import com.lina.securify.data.meta.MetaUser;
import com.lina.securify.data.models.NewUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * This class connects with Firebase to perform various authentication tasks.
 */
public class AuthRepository {

    private static final String TAG = AuthRepository.class.getSimpleName();

    private static AuthRepository instance;

    private FirebaseAuth firebaseAuth;

    public static AuthRepository getInstance() {
        if (instance == null)
            instance = new AuthRepository();

        return instance;
    }

    private AuthRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Checks if the email is associated with an existing account or not
     *
     * @param email The email to verify
     */
    public LiveData<Result> checkEmailExists(String email) {

        final MutableLiveData<Result> authResult = new MutableLiveData<>();

        firebaseAuth
                .fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        if (task.isSuccessful()) {

                            List<String> result = Objects.requireNonNull(
                                    task.getResult()
                            ).getSignInMethods();

                            if (!Objects.requireNonNull(result).isEmpty()) {

                                // An account with this email exists
                                authResult.setValue(Result.EXISTING_EMAIL);

                            } else {

                                // It's a new account
                                authResult.setValue(Result.NEW_EMAIL);
                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        authResult.setValue(Result.UNKNOWN_ERROR);

                        Log.e(TAG, "onFailure: Error checking sign in methods.", e);

                    }
                });

        return authResult;
    }

    /**
     * Signs in a user with Securify.
     *
     * @param email    The email of the user
     * @param password The password of the user
     */
    public LiveData<Result> signIn(String email, String password) {

        final MutableLiveData<Result> authResult = new MutableLiveData<>();

        firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            authResult.setValue(Result.SIGNED_IN);

                        } else {

                            handleAuthException(task.getException(), authResult);

                        }

                    }
                });


        return authResult;
    }

    /**
     * Signs up a user with Securify.
     * @param newUser The details about the new user
     */
    public LiveData<Result> signUp(final NewUser newUser) {

        final MutableLiveData<Result> authResult = new MutableLiveData<>();

        firebaseAuth
                .createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult _authResult) {

                        // Create a new user document
                        createNewUserDocument(newUser, authResult);

                    }
                });

        return authResult;
    }

    public LiveData<String> sendVerificationCode(String phoneNo) {

        final MutableLiveData<String> verificationId = new MutableLiveData<>();

        PhoneAuthProvider
                .getInstance(firebaseAuth)
                .verifyPhoneNumber(
                        phoneNo,
                        30,
                        TimeUnit.SECONDS,
                        TaskExecutors.MAIN_THREAD,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                Log.d(TAG, "Phone verification complete!");

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Log.w(TAG, "Phone verification failed!", e);

                                verificationId.setValue(null);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);

                                Log.d(TAG, "SMS code sent!");

                                verificationId.setValue(s);
                            }
                        }
                );

        return verificationId;

    }

    /**
     * Verifies the phone no. with the verification code.
     * @param smsCode The input code to verify
     */
    public LiveData<Result> verifySmsCode(String verificationCode, String smsCode) {

        final MutableLiveData<Result> authResult = new MutableLiveData<>();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(
                verificationCode, smsCode
        );

        firebaseAuth
                .getCurrentUser()
                .updatePhoneNumber(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        authResult.setValue(Result.PHONE_VERIFIED);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding phone no.!", e);

                        authResult.setValue(Result.INVALID_SMS_CODE);
                    }
                });

        return authResult;
    }

    private void createNewUserDocument(NewUser newUser, final MutableLiveData<Result> authResult) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String userID = getCurrentUserID();

        if (userID != null) {

            Map<String, String> user = new HashMap<>();
            user.put(MetaUser.FIRST_NAME, newUser.getFirstName());
            user.put(MetaUser.LAST_NAME, newUser.getLastName());

            firestore
                    .collection(Collections.USER)
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            authResult.setValue(Result.SIGNED_UP);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            authResult.setValue(Result.UNKNOWN_ERROR);

                            Log.e(TAG, "Error creating a user document!", e);
                        }
                    });

        } else {
            Log.e(TAG, "User not found!");
        }
    }

    @Nullable
    private String getCurrentUserID() {
        if (firebaseAuth.getCurrentUser() == null)
            return null;
        else
            return firebaseAuth.getCurrentUser().getUid();
    }

    /**
     * Handle the Firebase exceptions
     * @param _e The Exception object
     * @param authResult The auth result to be set based on exception
     */
    private void handleAuthException(Exception _e, MutableLiveData<Result> authResult) {

        try {
            throw _e;
        } catch (FirebaseAuthInvalidCredentialsException e) {

            /*
                There will be no weak password exception as it is already validated inside the UI.
                So, this exception is about wrong password.
             */

            authResult.setValue(Result.WRONG_PASSWORD);

        } catch (Exception e) {

            authResult.setValue(Result.UNKNOWN_ERROR);

            Log.e(TAG, "An unknown error occurred!", e);

        }

    }

    /**
     * Used to represent an authentication result.
     */
    public enum Result {

        /**
         * When an unknown error occurs
         */
        UNKNOWN_ERROR,

        /**
         * When the user is successfully signed up
         */
        SIGNED_UP,

        /**
         * When the user is successfully signed in
         */
        SIGNED_IN,

        /**
         * When the entered password is wrong
         */
        WRONG_PASSWORD,

        /**
         * When the entered email is already tied with an existing user
         */
        EXISTING_EMAIL,

        /**
         * When the entered email is not tied with an existing user
         */
        NEW_EMAIL,

        /**
         * When the phone no. is successfully verified
         */
        PHONE_VERIFIED,

        /**
         * The SMS code didn't match
         */
        INVALID_SMS_CODE
    }

}
