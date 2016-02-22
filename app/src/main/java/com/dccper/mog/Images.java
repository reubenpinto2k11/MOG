package com.dccper.mog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by reuben.pinto2k15 on 2/11/2016.
 */
public class Images implements Parcelable{
    private int mImg_id;
    private String mImg_name;
    private String mImg_path;

    public Images() {
        this.mImg_id=0;
        this.mImg_name="";
        this.mImg_path="";
    }

    public Images(int mImg_id, String mImg_name, String mImg_path) {
        this.mImg_id = mImg_id;
        this.mImg_name = mImg_name;
        this.mImg_path = mImg_path;
    }

    public int getmImg_id() {
        return mImg_id;
    }

    public void setmImg_id(int mImg_id) {
        this.mImg_id = mImg_id;
    }

    public String getmImg_name() {
        return mImg_name;
    }

    public void setmImg_name(String mImg_name) {
        this.mImg_name = mImg_name;
    }

    public String getmImg_path() {
        return mImg_path;
    }

    public void setmImg_path(String mImg_path) {
        this.mImg_path = mImg_path;
    }

    public Images(Parcel in)
    {
        String[] data=new String[3];
        in.readStringArray(data);
        this.mImg_id=Integer.parseInt(data[0]);
        this.mImg_name=data[1];
        this.mImg_path=data[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.mImg_id),this.mImg_name,this.mImg_path});
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Images createFromParcel(Parcel source) {
            return new Images(source);
        }

        @Override
        public Images[] newArray(int size) {
            return new Images[size];
        }
    };
}
