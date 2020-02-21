package com.lina.securify.data.models;

public class Alert {

    private static final String TAG = Alert.class.getSimpleName();

    private String location;
    private String victimName;
    private String victimPhone;

    public Alert() { }

    public Alert(
            String victimName,
            String victimPhone,
            String location) {

        this.victimName = victimName;
        this.victimPhone = victimPhone;
        this.location = location;

    }

    public String getVictimPhone() {
        return victimPhone;
    }

    public String getVictimName() {
        return victimName;
    }

    public String getLocation() {
        return location;
    }

    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }

    public void setVictimPhone(String victimPhone) {
        this.victimPhone = victimPhone;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
