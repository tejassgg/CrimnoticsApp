package com.example.crimnoticsapp.HelperClasses;

public class UserHelperClass {
    String name, contact, gender, email, aadhar, username,userType,userID;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String contact, String gender, String email, String aadhar, String username, String userType,String userID) {
        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.email = email;
        this.aadhar = aadhar;
        this.username = username;
        this.userType = userType;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
