package com.example.shareapp1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME="test_db";
    public static final String TABLE_NAME="test_table";

    private static final int DB_VERSION=1;

    private String CRATE_TEST_TABLE="CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + "(_id INTEGER PRIMARY KEY," + "content TEXT)";

    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CRATE_TEST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
