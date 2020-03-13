package com.lina.securify.sendalert;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lina.securify.R;
import com.lina.securify.data.FirestoreRepository;
import com.lina.securify.data.models.Alert;
import com.lina.securify.ResultListener;
import com.lina.securify.utils.constants.MetaUser;

public class AlertBuilder {

    private static final String TAG = AlertBuilder.class.getSimpleName();

    private Context context;
    private Alert alert;

    public AlertBuilder(Context context) {
        this.context = context;
    }

    public void build(ResultListener<Alert> listener) {

        alert = new Alert();

        Tasks.whenAll(taskLocation(), taskUserInfo())
                .addOnSuccessListener(_void -> {

                    listener.onResult(alert);

                })
                .addOnFailureListener(e -> {

                    listener.onResult(null);

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

        FirestoreRepository repository = FirestoreRepository.getInstance();

        return repository.getCurrentUserDocument().get()
                .addOnSuccessListener(documentSnapshot -> {
                    alert.setVictimName(
                            documentSnapshot.getString(MetaUser.FIRST_NAME) + " " +
                                    documentSnapshot.getString(MetaUser.LAST_NAME));

                    alert.setVictimPhone(documentSnapshot.getString(MetaUser.PHONE));
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user document!", e);
                });

    }

}
