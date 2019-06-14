package com.example.kota.task_manager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keisuke-ota on 2019/05/22.
 */

public class Task {
    private Integer id;
    private String title;
    private String description;
    private String limit_date;
    private Integer status_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLimitDate() {
        return limit_date;
    }

    public void setLimitDate(String limit_date) { this.limit_date = limit_date; }

    public Integer getStatusId() {
        return status_id;
    }

    public void setStatusId(Integer status_id) { this.status_id = status_id; }

    public static List<Task> findAllByConditionStr (SQLiteDatabase db, String conditionStr){
        //ステータスIDでタスクを検索し、結果をエンティティにセット
        Cursor cursor = db.query(
                "task",
                new String[] {"id", "title", "description", "limit_date", "status_id"},
                conditionStr,
                null,
                null,
                null,
                null
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
}
