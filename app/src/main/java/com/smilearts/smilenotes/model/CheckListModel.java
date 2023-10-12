package com.smilearts.smilenotes.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckListModel implements Serializable {

    int checkID;
    String title;
    ArrayList<Points> point;
    String reminder;
    String date;
    String time;
    String bg;
    int priority;

    public CheckListModel() {
    }

    public CheckListModel(int checkID, String title, ArrayList<Points> point, String reminder, String date, String time, String bg, int priority) {
        this.checkID = checkID;
        this.title = title;
        this.point = point;
        this.reminder = reminder;
        this.date = date;
        this.time = time;
        this.bg = bg;
        this.priority = priority;
    }

    public int getCheckID() {
        return checkID;
    }

    public void setCheckID(int checkID) {
        this.checkID = checkID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Points> getPoint() {
        return point;
    }

    public void setPoint(ArrayList<Points> point) {
        this.point = point;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public static class Points implements Serializable {

        String point;
        Boolean checkStatus;

        public Points() {

        }

        public Points(String point, Boolean checkStatus) {
            this.point = point;
            this.checkStatus = checkStatus;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public Boolean getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(Boolean checkStatus) {
            this.checkStatus = checkStatus;
        }

    }

}
