package com.lina.securify.data.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Volunteer extends BaseObservable {

    private static long ID_COUNTER = 1;

    private String phone;
    private String name;
    private long id;

    public Volunteer() {
        name = "";
        phone = "";

        id = ID_COUNTER++;
    }

    public Volunteer(String phone, String name) {
        this.phone = phone;
        this.name = name;

        id = ID_COUNTER++;
    }

    public Volunteer(Volunteer volunteer) {
        phone = volunteer.phone;
        name = volunteer.name;

        id = ID_COUNTER++;
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(com.lina.securify.BR.phone);
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(com.lina.securify.BR.name);
    }

}
