package com.lina.securify.views.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.lina.securify.R;

public class HomeViewModel extends AndroidViewModel {

    private final SharedPreferences preferences;

    private final MutableLiveData<Boolean> instantAlertState = new MutableLiveData<>();

    public HomeViewModel(Application application) {
        super(application);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());

        // Load Instant Alert state from preferences
        boolean isOn = preferences.getBoolean(
                getApplication().getString(R.string.pref_instant_alert), false);
        instantAlertState.setValue(isOn);
    }

    public LiveData<Boolean> getInstantAlertState() {
        return instantAlertState;
    }

    public void setInstantAlertState(boolean isOn) {
        instantAlertState.setValue(isOn);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getApplication().getString(R.string.pref_instant_alert), isOn);
        editor.apply();
    }

    public boolean isAppFresh() {
        String prefAppFresh = getApplication().getString(R.string.pref_fresh_app);
        return preferences.getBoolean(prefAppFresh, true);
    }

    public void makeAppStale() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getApplication().getString(R.string.pref_fresh_app), false);
        editor.apply();
    }

}
