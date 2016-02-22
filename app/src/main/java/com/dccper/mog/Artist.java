package com.dccper.mog;

/**
 * Created by reuben.pinto2k15 on 1/21/2016.
 */
public class Artist {
    private int nArtistID;
    private String mImagePath;
    private String mArtistName;
    private String mTextPath;
    private int mIsBooked;

    public Artist(int nArtistID, String mImagePath, String mArtistName, String mTextPath,int mIsBooked) {
        this.nArtistID = nArtistID;
        this.mImagePath = mImagePath;
        this.mArtistName = mArtistName;
        this.mTextPath = mTextPath;
        this.mIsBooked = mIsBooked;
    }

    public int getnArtistID() {
        return nArtistID;
    }

    public void setnArtistID(int nArtistID) {
        this.nArtistID = nArtistID;
    }

    public String getmImagePath() {
        return mImagePath;
    }

    public void setmImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public void setmArtistName(String mArtistName) {
        this.mArtistName = mArtistName;
    }

    public String getmTextPath() {
        return mTextPath;
    }

    public void setmTextPath(String mTextPath) {
        this.mTextPath = mTextPath;
    }

    public int getmIsBooked() {
        return mIsBooked;
    }

    public void setmIsBooked(int mIsBooked) {
        this.mIsBooked = mIsBooked;
    }
}
