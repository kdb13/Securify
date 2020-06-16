package com.lina.securify.views.viewmodels;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.lina.securify.data.repositories.VolunteersRepository;
import com.lina.securify.data.models.Volunteer;
import com.lina.securify.utils.Utils;

public class AddVolunteerViewModel extends AndroidViewModel {

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> phone = new ObservableField<>();
    
    private final VolunteersRepository volunteersRepository;
    
    public AddVolunteerViewModel(Application application) {
        super(application);
        volunteersRepository = VolunteersRepository.getInstance();
    }
    
    public void loadContactFromURI(Uri uri) {
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        };

        ContentResolver contentResolver = getApplication().getContentResolver();
        Cursor data = contentResolver.query(uri, projection,
                null, null, null);

        if (data != null && data.moveToNext()) {
            int numberIndex = data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            String name = data.getString(nameIndex);
            String number = data.getString(numberIndex);

            data.close();

            this.name.set(name);
            this.phone.set(Utils.trimPhone(number));
        }
    }
    
    public void addVolunteer() {
        Volunteer volunteer = new Volunteer(phone.get(), name.get());
        volunteersRepository.add(volunteer);
    }
    
}
