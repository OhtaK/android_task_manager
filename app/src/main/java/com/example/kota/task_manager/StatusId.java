package com.example.kota.task_manager;

/**
 * Created by keisuke-ota on 2019/05/23.
 */

public enum StatusId {
    TODO(1),
    DOING(2),
    DONE(3);

    private final int statusId;

    private StatusId(final int statusId) {
        this.statusId = statusId;
    }

    public int getValue() {
        return statusId;
    }

    public static StatusId getType(final int id) {
        StatusId[] ids = StatusId.values();
        for (StatusId idElem : ids) {
            if (idElem.getValue() == id) {
                return idElem;
            }
        }
        return null;
    }
}

