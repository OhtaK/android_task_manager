package com.example.kota.task_manager;

/**
 * Created by keisuke-ota on 2019/05/22.
 */

public class Task {
    private String title;
    private String description;
    private String limit_date;

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
}
