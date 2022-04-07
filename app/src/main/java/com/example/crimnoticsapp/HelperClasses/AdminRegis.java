package com.example.crimnoticsapp.HelperClasses;

public class AdminRegis {

    String name;
    String contact;
    String email;
    String area;
    String CISFNo;
    double latitude;
    double longitude;
    String userType;
    String userID;

    public AdminRegis() {
    }

    public AdminRegis(String name, String contact, String email, String area, String CISFNo, double latitude, double longitude, String userType,String userID) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.area = area;
        this.CISFNo = CISFNo;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCISFNo() {
        return CISFNo;
    }

    public void setCISFNo(String CISFNo) {
        this.CISFNo = CISFNo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
