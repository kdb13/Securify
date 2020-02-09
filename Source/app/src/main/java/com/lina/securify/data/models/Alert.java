package com.lina.securify.data.models;

public class Alert {

    private String victimName;
    private String victimPhone;
    private String message;
    private String location;
    private String sendDateTime;

    public Alert(
            String victimName,
            String victimPhone,
            String message,
            String location,
            String sendDateTime) {

        this.victimName = victimName;
        this.victimPhone = victimPhone;
        this.message = message;
        this.location = location;
        this.sendDateTime = sendDateTime;

    }

    public String getVictimPhone() {
        return victimPhone;
    }

    public String getVictimName() {
        return victimName;
    }

    public String getMessage() {
        return message;
    }

    public String getLocation() {
        return location;
    }

    public String getSendDateTime() {
        return sendDateTime;
    }
}
