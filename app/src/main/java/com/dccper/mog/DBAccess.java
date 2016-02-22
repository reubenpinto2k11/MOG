package com.dccper.mog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reuben.pinto2k15 on 2/9/2016.
 */
public class DBAccess {
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;
    private static DBAccess instance;

    private static final String TABLE_ARTISTS="artists";
    private static final String ARTIST_ID="_id";
    private static final String ARTIST_NAME="artist_name";
    private static final String ARTIST_TEXT_ID="text_id";
    private static final String ARTIST_IMAGE_ID="img_id";
    private static final String ARTIST_STATUS="status";
    private static final String ARTIST_ISBOOKED="isBooked";

    private static final String TABLE_IMG_FILES="img_files";
    private static final String IMG_FILE_ID="_id";
    private static final String IMG_FILE_NAME="img_name";
    private static final String IMG_FILE_PATH="img_path";

    private static final String TABLE_AUD_FILES="aud_files";
    private static final String AUD_FILE_ID="_id";
    private static final String AUD_FILE_NAME="aud_name";
    private static final String AUD_FILE_PATH="aud_path";

    private static final String TABLE_TXT_FILES="text_files";
    private static final String TXT_FILE_ID="_id";
    private static final String TXT_FILE_NAME="text_name";
    private static final String TXT_FILE_PATH="text_path";

    private static final String TABLE_INSTALLATION="installations";
    private static final String INST_ID="_id";
    private static final String INST_NAME="inst_name";
    private static final String INST_LOC="location";
    private static final String INST_QR_TAG="qr_tag";
    private static final String INST_ART_ID="artist_id";
    private static final String INST_ENG_AUD_ID="eng_aud_id";
    private static final String INST_HIN_AUD_ID="hin_aud_id";
    private static final String INST_MAR_AUD_ID="mar_aud_id";
    private static final String INST_TXT_ID="text_id";
    private static final String INST_ISBOOKED = "isBooked";

    private static final String TABLE_INST_IMG="inst_img";
    private static final String INST_IMG_ID="_id";
    private static final String INST_IMG_INST_ID="inst_id";
    private static final String INST_IMG_IMG_ID="img_id";


    public DBAccess(Context context) {
        this.openHelper=new DatabaseOpenHelper(context);
    }

    public static DBAccess getInstance(Context context)
    {
        if(instance == null)
            instance=new DBAccess(context);
        return instance;
    }

    public void open()
    {
        this.database=openHelper.getWritableDatabase();
    }

    public void close()
    {
        if(database != null)
            this.database.close();
    }

    public boolean isOpen()
    {
        if(database != null && database.isOpen())
            return database.isOpen();
        else
            return false;
    }

    public InstallationStruct getInstallation(String qrCode)
    {
        InstallationStruct ins=new InstallationStruct();
        String query="select * from installations inner join artists on artists._id=installations.artist_id inner join text_files on text_files._id=installations.text_id where installations.qr_tag like ?";
        Cursor cursor=database.rawQuery(query, new String[]{qrCode});
        cursor.moveToFirst();
        ins.set_id(cursor.getInt(0));
        ins.setInst_name(cursor.getString(1));
        ins.setLocation(cursor.getInt(2));
        ins.setQr_tag(cursor.getString(3));
        ins.setArtst_name(cursor.getString(11));
        ins.setText_path(cursor.getString(17));
        ins.setEng_aud(cursor.getInt(5),"","");
        ins.setHin_aud(cursor.getInt(6),"","");
        ins.setMar_aud(cursor.getInt(7),"","");
        ins.setmIsBooked(cursor.getInt(9));
        cursor.close();
        return ins;
    }

    public Audio resetAudioDat(Audio file)
    {
        String query="select aud_files.aud_name,aud_files.aud_path from aud_files where aud_files._id=?";
        Cursor cursor=database.rawQuery(query,new String[]{String.valueOf(file.getmAud_id())});
        cursor.moveToFirst();
        file.setmAud_name(cursor.getString(0));
        file.setmAud_path(cursor.getString(1));
        cursor.close();
        return file;
    }

    public ArrayList<Images> getInstImages(int id)
    {
        ArrayList<Images> nList=new ArrayList<>();
        String query="select img_files._id,img_files.img_name,img_files.img_path from inst_img inner join img_files on img_files._id=inst_img.img_id where inst_img.inst_id=?";
        Cursor cursor=database.rawQuery(query,new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            nList.add(new Images(cursor.getInt(0),cursor.getString(1),cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();
        return nList;
    }

    public ArrayList<Artist> getArtists()
    {
        ArrayList<Artist> artList=new ArrayList<>();
        String query="select * from artists inner join text_files on text_files._id=artists.text_id inner join img_files on img_files._id=artists.img_id where status=1";
        Cursor cursor=database.rawQuery(query,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            //artist(id,img_path,art_name,text_path_isbookmarked)
            artList.add(new Artist(cursor.getInt(0),cursor.getString(11),cursor.getString(1),cursor.getString(8),cursor.getInt(5)));
            cursor.moveToNext();
        }
        cursor.close();
        return artList;
    }

    public Artist getSingleArtist(int mArtID)
    {
        Artist artist;
        String query="select * from artists inner join text_files on text_files._id=artists.text_id inner join img_files on img_files._id=artists.img_id where artists._id=?";
        Cursor cursor=database.rawQuery(query,new String[]{String.valueOf(mArtID)});
        cursor.moveToFirst();
        artist=new Artist(cursor.getInt(0),cursor.getString(11),cursor.getString(1),cursor.getString(8),cursor.getInt(5));
        cursor.close();
        return artist;
    }

    public ArrayList<String> getRelatedInst(int mArtID)
    {
        ArrayList<String> list=new ArrayList<>();
        String query="select installations.inst_name from installations where installations.artist_id=?";
        Cursor cursor=database.rawQuery(query,new String[]{String.valueOf(mArtID)});
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getFloorInstalnList(int floor)
    {
        List<String> mList=new ArrayList<>();
        String query="select installations.inst_name from installations where installations.location=?";
        Cursor cursor=database.rawQuery(query,new String[]{String.valueOf(floor)});
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            mList.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return mList;
    }

    public void updateInstallation(long ins_id,String toggle_book)
    {
        ContentValues cv=new ContentValues();
        if(toggle_book.equals("bookmark"))
            cv.put(INST_ISBOOKED,(long)1);
        else
            cv.put(INST_ISBOOKED,(long)0);
        database.update(TABLE_INSTALLATION,cv,"_id="+ins_id,null);
    }

    public void updateArtist(long ins_id,String toggle_book)
    {
        ContentValues cv=new ContentValues();
        if(toggle_book.equals("bookmark"))
            cv.put(ARTIST_ISBOOKED,(long)1);
        else
            cv.put(ARTIST_ISBOOKED,(long)0);
        database.update(TABLE_ARTISTS,cv,"_id="+ins_id,null);
    }
}
