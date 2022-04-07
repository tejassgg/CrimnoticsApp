package com.example.crimnoticsapp.HelperClasses;

public class Missing {

    String postKey;
    String name;
    String age;
    String height;
    String lastSeen;
    String gender;
    String description;
    String missingImage;
    String userID;
    String reportDate,reportTime;
    Object timeStamp;
    String application;

    public Missing() {
    }

    public Missing(String name, String age, String height, String lastSeen, String gender, String description, String missingImage, String userID, String reportDate, String reportTime, String application) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.lastSeen = lastSeen;
        this.gender = gender;
        this.description = description;
        this.missingImage = missingImage;
        this.userID = userID;
        this.reportDate = reportDate;
        this.reportTime = reportTime;
        this.timeStamp = (-1)*System.currentTimeMillis();
        this.application = application;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMissingImage() {
        return missingImage;
    }

    public void setMissingImage(String missingImage) {
        this.missingImage = missingImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
