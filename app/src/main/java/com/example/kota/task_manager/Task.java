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
}
