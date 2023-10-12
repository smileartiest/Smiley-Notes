package com.smilearts.smilenotes.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Recycle")
public class RecycleModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int Id;
    @ColumnInfo(name = "Title")
    private String Title = "";
    @ColumnInfo(name = "Message")
    private String Message = "";
    @ColumnInfo(name = "Date")
    private String Date = "";

    public RecycleModel() {
    }

    public RecycleModel(int id, String title, String message, String date) {
        Id = id;
        Title = title;
        Message = message;
        Date = date;
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

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
