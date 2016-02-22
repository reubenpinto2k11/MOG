package com.dccper.mog;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by reuben.pinto2k15 on 2/9/2016.
 */
public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME="MOG_Home.db";
    private static final int DATABASE_VERSION=1;

    public DatabaseOpenHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }
}
