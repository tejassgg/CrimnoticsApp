package com.example.crimnoticsapp.HelperClasses;

public class Report {

    String postKey;
    String type;
    double latitude;
    double longitude;
    String address;
    String description;
    String crimeImage;
    String userID;
    String reportDate,reportTime;
    Object timeStamp;
    String application;
    double dist;

    public Report(String type, double latitude, double longitude, String address, String description, String crimeImage, String userID, String reportTime, String reportDate,String application, double dist) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.description = description;
        this.crimeImage = crimeImage;
        this.userID = userID;
        this.reportTime = reportTime;
        this.reportDate = reportDate;
        this.timeStamp = (-1)*System.currentTimeMillis();
        this.application = application;
        this.dist = dist;
    }

    public Report() {
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCrimeImage() {
        return crimeImage;
    }

    public void setCrimeImage(String crimeImage) {
        this.crimeImage = crimeImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
