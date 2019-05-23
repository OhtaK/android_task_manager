package com.example.kota.task_manager;

/**
 * Created by keisuke-ota on 2019/05/23.
 */

enum StatusId {
    TODO(1),
    DOING(2),
    DONE(3);

    private final int statusId;

    private StatusId(int statusId) {
        this.statusId = statusId;
    }

    int getStatusId() {
        return statusId;
    }
}

