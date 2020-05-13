package com.lina.securify.sendalert;

/**
 * Constitutes the data that make up the Alert SMS.
 */
class AlertData {

    private String victimName;
    private String victimPhone;
    private String location;

    String getVictimName() {
        return victimName;
    }

    String getVictimPhone() {
        return victimPhone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    void setVictimName(String victimName) {
        this.victimName = victimName;
    }

    void setVictimPhone(String victimPhone) {
        this.victimPhone = victimPhone;
    }
}
