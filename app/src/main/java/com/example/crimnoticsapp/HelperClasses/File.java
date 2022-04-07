package com.example.crimnoticsapp.HelperClasses;

public class File {

    String postKey;
    String type;
    String area;
    String description;
    String userID;
    String reportDate,reportTime;
    Object timeStamp;

    public File() {
    }

    public File(String type, String area, String description, String userID, String reportDate, String reportTime) {
        this.type = type;
        this.area = area;
        this.description = description;
        this.userID = userID;
        this.reportDate = reportDate;
        this.reportTime = reportTime;
        this.timeStamp = (-1)*System.currentTimeMillis();

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
