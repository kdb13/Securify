package com.lina.securify.sendalert;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lina.securify.R;
import com.lina.securify.contracts.UsersContract;
import com.lina.securify.contracts.VolunteersContract;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * It contains the tasks required in sending the alert to volunteers. To start the process,
 * call the begin() method.
 */
public class SendAlertTasks {

    private static final String TAG = SendAlertTasks.class.getSimpleName();

    private SendAlertTasks() { }

    /**
     * Creates an AlertData object required for sending the alert. It takes as input the current
     * user's DocumentSnapshot and Location.
     */
    private static class CreateAlert implements Continuation<List<Object>, AlertData> {

        private Context context;

        CreateAlert(Context context) {
            this.context = context;
        }

        @Override
        public AlertData then(@NonNull Task<List<Object>> task) throws Exception {

            List<Object> tasks = task.getResult();

            DocumentSnapshot documentSnapshot = (DocumentSnapshot) tasks.get(0);
            Location location = (Location) tasks.get(1);

            AlertData alertData = new AlertData();

            // Get the user information
            alertData.setVictimName(documentSnapshot.getString(UsersContract.FIRST_NAME) + " " +
                    documentSnapshot.getString(UsersContract.LAST_NAME));
            alertData.setVictimPhone(documentSnapshot.getString(UsersContract.PHONE));

            // Get current location
            if (location != null)
                alertData.setLocation(Utils.formatLocation(
                        location.getLatitude(), location.getLongitude()));
            else
                alertData.setLocation(context.getString(R.string.alert_no_location));

            return alertData;

        }
    }

    /**
     * Starts the process of executing tasks involved in sending the alert.
     * @param context The application Context
     */
    public static void begin(Context context) {
        final FirestoreRepository repository = FirestoreRepository.getInstance();

        Task<DocumentSnapshot> taskFetchUser = repository.getCurrentUserDocument().get();
        Task<Location> taskLocation = LocationServices.getFusedLocationProviderClient(context)
                .getLastLocation();

        // TASK: Getting the volunteers' phone numbers
        Task<List<String>> taskGetPhones = repository.getVolunteers().get()
                .continueWith(task -> {

                    // If the user doesn't have any volunteers
                    if (task.getResult().isEmpty())
                        throw new NoVolunteersException("No volunteers found!");

                    List<String> phones = new ArrayList<>();

                    for (DocumentSnapshot document : task.getResult())
                        phones.add(document.getString(VolunteersContract.PHONE));

                    return phones;

                });

        // TASK: Build the Alert SMS
        Task<AlertData> taskCreateAlert = Tasks.whenAllSuccess(taskFetchUser, taskLocation)
                .continueWith(new CreateAlert(context));

        // Combining tasks
        Tasks.whenAllSuccess(taskGetPhones, taskCreateAlert)
                .addOnSuccessListener(objects -> {

                    List<String> phones = (List<String>) objects.get(0);
                    AlertData alertData = (AlertData) objects.get(1);

                    String alertSms = new AlertSmsBuilder(context, alertData).build();

                    new AlertSmsSender(alertSms, phones).send(context);

                })
                .addOnFailureListener(e -> {

                    if (e instanceof NoVolunteersException)
                        Utils.showToast(context, e.getMessage());
                    else
                        Log.e(TAG, "Error executing tasks!", e);

                });

    }

    /**
     * This exception is thrown if the user doesn't have one or more volunteers.
     */
    private static class NoVolunteersException extends Exception {

        NoVolunteersException(String message) {
            super(message);
        }

    }

}
