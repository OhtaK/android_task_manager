package com.example.kota.task_manager;

/**
 * Created by keisuke-ota on 2019/05/03.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskSQLiteOpenHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 3;

    // データーベース情報を変数に格納
    private static final String DATABASE_NAME = "taskManager.db";
    private static final String TABLE_NAME = "task";
    private static final String ID = "id";
    private static final String COLUMN_NAME_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LIMIT_DATE = "limit_date";
    private static final String STATUS_ID = "status_id";
    private static final String COLUMN_CREATE_DATE = "create_date";
    private static final String COLUMN_UPDATE_DATE = "update_date";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_LIMIT_DATE + " TEXT," +
                    STATUS_ID + " TEXT," +
                    COLUMN_CREATE_DATE + " TEXT," +
                    COLUMN_UPDATE_DATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;


    TaskSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //すでにテーブルが作られていれば呼ばれない
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

    public static void saveData(SQLiteDatabase db, Integer id, String title, String description, String limitDate, Integer statusId){
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        String currentDate = df.format(date);

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("title", title);
        values.put("description", description);
        values.put("limit_date", limitDate);
        values.put("status_id", String.valueOf(statusId));
        values.put("create_date", currentDate);
        values.put("update_date", currentDate);

        db.insert(TABLE_NAME, null, values);
    }

    public static void updateById(SQLiteDatabase db, Integer id, String title, String description, String limit_date, Integer status_id){
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        String currentDate = df.format(date);

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("title", title);
        values.put("description", description);
        values.put("limit_date", limit_date);
        values.put("status_id", String.valueOf(status_id));
        values.put("update_date", currentDate);

        db.update("task", values, "id = " + id, null);
    }

    public static void deleteById(SQLiteDatabase db, Integer id){
        db.delete("task", "id = " + id, null);
    }

    public static List<Task> findAllByConditionStr (SQLiteDatabase db, String condition, String order){
        //ステータスIDでタスクを検索し、結果をエンティティにセット
        Cursor cursor = db.query(
                "task",
                new String[] {"id", "title", "description", "limit_date", "status_id"},
                condition,
                null,
                null,
                null,
                order
        );

        //結果格納用のリスト
        List<Task> taskList = new ArrayList<Task>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Task task = new Task();
            task.setId(cursor.getInt(cursor.getColumnIndex("id")));
            task.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            task.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            task.setLimitDate(cursor.getString(cursor.getColumnIndex("limit_date")));
            task.setStatusId(Integer.valueOf(cursor.getString(cursor.getColumnIndex("status_id"))));

            taskList.add(task);
            cursor.moveToNext();
        }
        cursor.close();

        return taskList;
    }

    public static Task findByTaskId (SQLiteDatabase db, Integer taskId){
        //ステータスIDでタスクを検索し、結果をエンティティにセット
        Cursor cursor = db.query(
                "task",
                new String[] {"id", "title", "description", "limit_date", "status_id", "create_date", "update_date"},
                "id = " + String.valueOf(taskId),
                null,
                null,
                null,
                null
        );

        //結果格納用のリスト
        cursor.moveToFirst();
        Task task = new Task();
        task.setId(cursor.getInt(cursor.getColumnIndex("id")));
        task.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        task.setDescription(cursor.getString(cursor.getColumnIndex("description")));
        task.setLimitDate(cursor.getString(cursor.getColumnIndex("limit_date")));
        task.setStatusId(Integer.valueOf(cursor.getString(cursor.getColumnIndex("status_id"))));
        cursor.close();

        return task;
    }

    public static Integer fetchLastId (SQLiteDatabase db){
        //最新のIDを検索
        Cursor cursor = db.query(
                "task",
                new String[] {"id"},
                null,
                null,
                null,
                null,
                "id desc",
                "1"
        );
        Integer lastId = 0;
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            lastId = cursor.getInt(cursor.getColumnIndex("id"));
        }
        cursor.close();

        return lastId;
    }

    public static String buildSelectionStr(Map<String, String> conditionMap){
        //検索条件をmapで渡すとSQLiteのqueryに入れるStringを生成。配列をforeachで回してconditionを生成していく。
        //値が入ってなかったら条件に入れない
        String result = "";
        int index = 0;
        for (String key: conditionMap.keySet()) {
            if(conditionMap.get(key).isEmpty()){
                continue;
            }
            if(index != 0){
                result += "and ";
            }

            switch(key){
                case "status_id":
                    result += "status_id =" + conditionMap.get(key) + " ";
                    break;

                case "limit_date_start":
                    result += "limit_date >= '" + conditionMap.get(key) + "' ";
                    break;

                case "limit_date_end":
                    result += "limit_date <= '" + conditionMap.get(key) + "' ";
                    break;
            }
            index++;
        }

        return result;
    }
}
