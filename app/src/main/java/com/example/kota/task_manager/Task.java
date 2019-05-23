package com.example.kota.task_manager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keisuke-ota on 2019/05/22.
 */

public class Task {
    private String title;
    private String description;
    private String limit_date;
    private Integer status_id;

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

    public static List<Task> findAllByStatusId (SQLiteDatabase db, Integer statusId){
        //ステータスIDでタスクを検索し、結果をエンティティにセット

        Cursor cursor = db.query(
                "task",
                new String[] {"title", "description", "limit_date", "status_id", "create_date", "update_date"},
                "status_id = " + String.valueOf(statusId),
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
            task.setTitle(cursor.getString(0));
            task.setDescription(cursor.getString(1));
            task.setLimitDate(cursor.getString(2));
            task.setStatusId(Integer.valueOf(cursor.getString(3)));
            taskList.add(task);
            cursor.moveToNext();
        }
        cursor.close();

        return taskList;
    }
}
