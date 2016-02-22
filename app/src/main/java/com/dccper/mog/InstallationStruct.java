package com.dccper.mog;

/**
 * Created by reuben.pinto2k15 on 2/9/2016.
 */
public class InstallationStruct {
    private int _id;
    private String inst_name;
    private int location;
    private String qr_tag;
    private String artst_name;
    private Audio eng_aud;
    private Audio hin_aud;
    private Audio mar_aud;
    private String text_path;
    private int mIsBooked;

    public InstallationStruct() {
        this.eng_aud = new Audio();
        this.hin_aud = new Audio();
        this.mar_aud = new Audio();
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getInst_name() {
        return inst_name;
    }

    public void setInst_name(String inst_name) {
        this.inst_name = inst_name;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getQr_tag() {
        return qr_tag;
    }

    public void setQr_tag(String qr_tag) {
        this.qr_tag = qr_tag;
    }

    public String getArtst_name() {
        return artst_name;
    }

    public void setArtst_name(String artst_name) {
        this.artst_name = artst_name;
    }

    public Audio getEng_aud() {
        return eng_aud;
    }

    public void setEng_aud(int id,String name,String path) {
        this.eng_aud.setmAud_id(id) ;
        this.eng_aud.setmAud_name(name);
        this.eng_aud.setmAud_path(path);
    }

    public void setEng_aud(Audio aud)
    {
        this.eng_aud=aud;
    }

    public Audio getHin_aud() {
        return hin_aud;
    }

    public void setHin_aud(int id,String name,String path) {
        this.hin_aud.setmAud_id(id);
        this.hin_aud.setmAud_name(name);
        this.hin_aud.setmAud_path(path);
    }

    public void setHin_aud(Audio aud)
    {
        this.hin_aud=aud;
    }

    public Audio getMar_aud() {
        return mar_aud;
    }

    public void setMar_aud(int id,String name,String path) {
        this.mar_aud.setmAud_id(id);
        this.mar_aud.setmAud_name(name);
        this.mar_aud.setmAud_path(path);
    }

    public void setMar_aud(Audio aud)
    {
        this.mar_aud=aud;
    }

    public String getText_path() {
        return text_path;
    }

    public void setText_path(String text_path) {
        this.text_path = text_path;
    }

    public int getmIsBooked() {
        return mIsBooked;
    }

    public void setmIsBooked(int mIsBooked) {
        this.mIsBooked = mIsBooked;
    }
}
