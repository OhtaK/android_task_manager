package com.example.kota.task_manager;

/**
 * Created by keisuke-ota on 2019/05/03.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper  extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 2;

    // データーベース情報を変数に格納
    private static final String DATABASE_NAME = "taskManager.db";
    private static final String TABLE_NAME = "task";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LIMIT_DATE = "limit_date";
    private static final String STATUS_ID = "status_id";
    private static final String COLUMN_CREATE_DATE = "create_date";
    private static final String COLUMN_UPDATE_DATE = "update_date";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_LIMIT_DATE + " TEXT," +
                    STATUS_ID + " TEXT," +
                    COLUMN_CREATE_DATE + " TEXT," +
                    COLUMN_UPDATE_DATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;


    MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        //テーブルの存在確認
//        String query = "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='task';";
//        Cursor c = db.rawQuery(query, null);
//        c.moveToFirst();
//        String result = c.getString(0);
//        Log.d("result", result);
//
//        //存在してなければ作成
//        if(result == "0") {
//            db.execSQL(
//                    SQL_CREATE_ENTRIES
//            );
//        }

        db.execSQL(
                SQL_CREATE_ENTRIES
        );
    }

    // 参考：https://sankame.github.io/blog/2017-09-05-android_sqlite_db_upgrade/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void saveData(SQLiteDatabase db, String title, String description, String limit_date, Integer status_id, String create_date, String update_date){
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("limit_date", limit_date);
        values.put("status_id", String.valueOf(status_id));
        values.put("create_date", create_date);
        values.put("update_date", update_date);

        db.insert(TABLE_NAME, null, values);
    }
}