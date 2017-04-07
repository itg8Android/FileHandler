package com.itg.filehandler.model;

import android.os.Parcel;
import android.os.Parcelable;



public class FolderItem implements Parcelable {
    private String fileName;
    private String filePath;
    private String size;
    private int type;




    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileName);
        dest.writeString(this.filePath);
        dest.writeString(this.size);
        dest.writeInt(this.type);
    }

    public FolderItem() {
    }

    private FolderItem(Parcel in) {
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.size = in.readString();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<FolderItem> CREATOR = new Parcelable.Creator<FolderItem>() {
        @Override
        public FolderItem createFromParcel(Parcel source) {
            return new FolderItem(source);
        }

        @Override
        public FolderItem[] newArray(int size) {
            return new FolderItem[size];
        }
    };
}
