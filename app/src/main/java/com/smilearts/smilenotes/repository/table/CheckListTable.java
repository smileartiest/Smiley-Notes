package com.smilearts.smilenotes.repository.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "CheckList")
public class CheckListTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int Id;
    @ColumnInfo(name = "Title")
    private String Title = "";
    @ColumnInfo(name = "Points")
    private String Points = "";
    @ColumnInfo(name = "Remainder")
    private String Remainder = "";
    @ColumnInfo(name = "Date")
    private String Date = "";
    @ColumnInfo(name = "Time")
    private String Time = "";
    @ColumnInfo(name = "Bg")
    private String Bg = "";
    @ColumnInfo(name = "Priority")
    private int Priority = 0;

    public CheckListTable() {
    }

    public CheckListTable(int id, String title, String points, String remainder, String date, String time, String bg, int priority) {
        Id = id;
        Title = title;
        Points = points;
        Remainder = remainder;
        Date = date;
        Time = time;
        Bg = bg;
        Priority = priority;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }

    public String getRemainder() {
        return Remainder;
    }

    public void setRemainder(String remainder) {
        Remainder = remainder;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getBg() {
        return Bg;
    }

    public void setBg(String bg) {
        Bg = bg;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
    }

}
