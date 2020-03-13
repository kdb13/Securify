package com.lina.securify.sendalert;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lina.securify.R;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.models.Alert;
import com.lina.securify.utils.constants.MetaUser;
import com.lina.securify.utils.constants.MetaVolunteer;

import java.util.ArrayList;
import java.util.List;

public class AlertSender {

    private static final String TAG = AlertSender.class.getSimpleName();

    Alert alert = new Alert();
    private Context context;
    private FirestoreRepository repository = FirestoreRepository.getInstance();

    public AlertSender(Context context) {
        this.context = context;
    }

    private void currentLocation() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(response -> {

                    FusedLocationProviderClient client =
                            LocationServices.getFusedLocationProviderClient(context);

                    client.getLastLocation().addOnSuccessListener(location -> {


                    });
                })
                .addOnFailureListener(e -> {

                });

    }

    public void perform() {

        List<String> volunteerPhones = new ArrayList<>();

        repository.getVolunteers().get()
                .addOnSuccessListener(documentSnapshots -> {

                    if (!documentSnapshots.isEmpty()) {

                        for (DocumentSnapshot snapshot : documentSnapshots)
                            volunteerPhones.add(snapshot.getString(MetaVolunteer.PHONE));

                        Tasks.whenAll(taskLocation(), taskUserInfo())
                                .addOnSuccessListener(task -> {

                                    Log.d(TAG, "Tasks complete!");

                                });

                    } else {
                        Log.i(TAG, "No volunteers found!");
                    }

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching volunteers!", e);
                });
    }

    private Task<Location> taskLocation() {
        FusedLocationProviderClient locationProviderClient =
                LocationServices.getFusedLocationProviderClient(context);

        return locationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        alert.setLocation(location.getLatitude() + "," + location.getLongitude());
                    } else {
                        alert.setLocation(context.getString(R.string.alert_no_location));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching location!", e);
                });
    }

    private Task<DocumentSnapshot> taskUserInfo() {

        return repository.getCurrentUserDocument().get()
                .addOnSuccessListener(documentSnapshot -> {
                    alert.setVictimName(
                            documentSnapshot.getString(MetaUser.FIRST_NAME) + " " +
                            documentSnapshot.getString(MetaUser.LAST_NAME));
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user document!", e);
                });

    }
}
