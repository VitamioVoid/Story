package com.qf.story.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public final static String DATABASE = "story.db";
    public final static String TABLENAME = "record";
    private final static String CREATE_STR = "create table record(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "portrait VARCHAR," +
            "gender INTEGER," +
            "alias VARCHAR," +
            "place VARCHAR," +
            "uptime VARCHAR," +
            "story VARCHAR," +
            "image VARCHAR," +
            "readCount INTEGER," +
            "commentCount INTEGER)";

    public DBHelper(Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
