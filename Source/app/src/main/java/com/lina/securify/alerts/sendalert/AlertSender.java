package com.lina.securify.alerts.sendalert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lina.securify.R;
import com.lina.securify.data.repositories.UsersRepository;
import com.lina.securify.data.repositories.VolunteersRepository;
import com.lina.securify.alerts.Victim;
import com.lina.securify.data.contracts.UsersContract;
import com.lina.securify.data.contracts.VolunteersContract;
import com.lina.securify.alerts.showalert.AlertSmsReceiver;
import com.lina.securify.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlertSender {

    private static final String TAG = "AlertSender";

    private final Context context;

    public AlertSender(Context context) {
        this.context = context;
    }

    public void send() {

        // Return if user is has not signed in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        if (Utils.isSDK(Build.VERSION_CODES.M)) {

            ArrayList<String> requiredPermissions = new ArrayList<>();
            requiredPermissions.add(Manifest.permission.SEND_SMS);
            requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            requiredPermissions.add(Manifest.permission.READ_PHONE_STATE);

            if (Utils.isSDK(Build.VERSION_CODES.Q)) {
                requiredPermissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            }

            if (!Utils.arePermissionsGranted(context, requiredPermissions)) {

                Utils.showToast(context, "Missing required permissions!");

                return;
            }

        }

        VolunteersRepository volunteersRepository = VolunteersRepository.getInstance();
        UsersRepository usersRepository = UsersRepository.getInstance();
        FusedLocationProviderClient locationProviderClient =
                LocationServices.getFusedLocationProviderClient(context);


        // TASK: Fetch volunteers
        final Task<QuerySnapshot> taskFetchVolunteers = volunteersRepository.queryVolunteers().get();

        // TASK: Extract volunteers' phones
        final Continuation<QuerySnapshot, List<String>> extractPhones = task -> {
            QuerySnapshot querySnapshot = Objects.requireNonNull(task.getResult());

            if (querySnapshot.isEmpty()) {
                throw new NoVolunteersException();
            }

            List<String> phones = new ArrayList<>();

            for (DocumentSnapshot snapshot : querySnapshot) {
                phones.add(snapshot.getString(VolunteersContract.PHONE));
            }

            return phones;
        };

        // TASK: Fetch phones of volunteers
        final Task<List<String>> taskFetchPhonesOfVolunteers =
                taskFetchVolunteers.continueWith(extractPhones);

        // TASK: Fetch user
        final Task<DocumentSnapshot> taskFetchUser = usersRepository.getCurrentUser().get();

        // TASK: Fetch current location
        @SuppressLint("MissingPermission") final Task<Location> taskFetchLocation = locationProviderClient.getLastLocation();

        // TASK: Extract coordinates from the location
        final Continuation<Location, String> extractCoordinates = task -> {

            Location location = task.getResult();

            if (location == null) {
                return null;
            } else {
                return location.getLatitude() + "," + location.getLongitude();
            }

        };

        // TASK: Fetch coordinates of current location
        final Task<String> taskFetchLocationCoordinates =
                taskFetchLocation.continueWith(extractCoordinates);

        // All tasks in parallel
        final List<Task<?>> tasks = new ArrayList<>();

        tasks.add(taskFetchPhonesOfVolunteers);
        tasks.add(taskFetchUser);
        tasks.add(taskFetchLocationCoordinates);

        Tasks
                .whenAll(tasks)
                .addOnSuccessListener(results -> {

                    List<String> phones =
                            Objects.requireNonNull(taskFetchPhonesOfVolunteers.getResult());

                    DocumentSnapshot userSnapshot =
                            Objects.requireNonNull(taskFetchUser.getResult());

                    String locationCoordinates =
                            taskFetchLocationCoordinates.getResult();

                    final String fullName = userSnapshot.getString(UsersContract.FIRST_NAME) +
                            " " + userSnapshot.getString(UsersContract.LAST_NAME);

                    Victim victim = new Victim();
                    victim.setName(fullName);
                    victim.setPhone(userSnapshot.getString(UsersContract.PHONE));
                    victim.setLocation(locationCoordinates);

                    sendAlertSMS(buildAlertSMS(victim), phones);

                })
                .addOnFailureListener(e -> {

                    if (e.getCause() instanceof NoVolunteersException) {
                        Utils.showToast(context, e.getCause().getMessage());
                    }

                });


    }

    private String buildAlertSMS(Victim victim) {

        String alertHeader = context.getString(R.string.alert_header,
                context.getString(R.string.app_name).toUpperCase());

        String alertLocation = context.getString(R.string.alert_no_location);

        if (victim.getLocation() != null)
            alertLocation = context.getString(R.string.google_maps_url) + victim.getLocation();

        String location = context.getString(R.string.alert_location,
                context.getString(R.string.alert_current_location),
                alertLocation);

        String description = context.getString(R.string.alert_description,
                context.getString(R.string.alert_help_message),
                victim.getName(),
                victim.getPhone());

        return context.getString(R.string.alert_sms,
                alertHeader, description, location);

    }

    private void sendAlertSMS(String alertSMS, List<String> phones) {

        SmsManager smsManager = SmsManager.getDefault();

        for (String phone : phones) {

            smsManager.sendTextMessage(
                    phone, null, alertSMS, null, null);

        }

    }

    private static class NoVolunteersException extends Exception {

        public NoVolunteersException() {
            super("No volunteers found!");
        }

    }

}
