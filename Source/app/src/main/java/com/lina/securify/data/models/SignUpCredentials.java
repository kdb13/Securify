package com.lina.securify.data.models;

public class SignUpCredentials extends LoginCredentials {

    private String firstName;
    private String lastName;
    private String confPassword;
    private String phone;
    private String otpCode;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public String getPhone() {
        return phone;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}
