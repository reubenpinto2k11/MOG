package com.dccper.mog;

/**
 * Created by reuben.pinto2k15 on 2/9/2016.
 */
public class Audio {
    private int mAud_id;
    private String mAud_name;
    private String mAud_path;

    public Audio()
    {
        mAud_id=0;
        mAud_name="";
        mAud_path="";
    }

    public int getmAud_id() {
        return mAud_id;
    }

    public void setmAud_id(int mAud_id) {
        this.mAud_id = mAud_id;
    }

    public String getmAud_name() {
        return mAud_name;
    }

    public void setmAud_name(String mAud_name) {
        this.mAud_name = mAud_name;
    }

    public String getmAud_path() {
        return mAud_path;
    }

    public void setmAud_path(String mAud_path) {
        this.mAud_path = mAud_path;
    }
}
