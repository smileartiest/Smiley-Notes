package com.smilearts.smilenotes.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ImageNotes")
public class ImageModel {

    @PrimaryKey(autoGenerate = true)
    private int ImageID;

    @ColumnInfo(name = "Title")
    private String Title;

    @ColumnInfo(name = "Image" , typeAffinity = ColumnInfo.BLOB)
    private byte[] Image;

    public ImageModel() {
    }

    public ImageModel(int imageID, String title, byte[] image) {
        ImageID = imageID;
        Title = title;
        Image = image;
    }

    public int getImageID() {
        return ImageID;
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }
}
