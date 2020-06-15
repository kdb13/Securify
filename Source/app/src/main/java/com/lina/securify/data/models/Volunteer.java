package com.lina.securify.data.models;

public class Volunteer {

    private String phone;
    private String name;

    // Required by FirestoreRecyclerAdapter
    public Volunteer() { }

    public Volunteer(String phone, String name) {
        this.phone = phone;
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

}
