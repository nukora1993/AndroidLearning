package com.example.shareapp1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ReadWriteProvider extends ContentProvider {
    private static String DATA_DIR="";



    public static final String AUTHORITY="com.example.shareapp1.readwrite.provider";


    private Context context;
    private SQLiteDatabase db;



    public ReadWriteProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
//        throw new UnsupportedOperationException("Not yet implemented");
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
//        throw new UnsupportedOperationException("Not yet implemented");
        db.insert(DBHelper.TABLE_NAME,null,values);

        return uri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        context=getContext();
        db=new DBHelper(context).getWritableDatabase();
        DATA_DIR=getContext().getExternalCacheDir().getAbsolutePath();
        String data=DataUtils.readData(DATA_DIR);
        db.execSQL("insert or ignore into 'test_table' values(1,"+"'"+data+"'"+")");

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
//        throw new UnsupportedOperationException("Not yet implemented");

        Cursor cursor=db.query(DBHelper.TABLE_NAME,projection,selection,selectionArgs,null
        ,null,sortOrder,null);
        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
//        throw new UnsupportedOperationException("Not yet implemented");
        int row=db.update(DBHelper.TABLE_NAME,values,selection,selectionArgs);
        return row;
    }
}
