package com.batherphilippa.pin_it_app_be.model;

public enum TaskStatus {
    PENDING("Pending", 1),
    IN_PROGRESS("In Progress", 2),
    BLOCKED("Blocked", 3),
    COMPLETED("Completed", 4);

    private final String statusName;
    private final int statusNum;

    TaskStatus(String statusName, int statusNum) {
        this.statusName = statusName;
        this.statusNum = statusNum;
    }

    public String getStatusName() {
        return this.statusName;
    }

    public int getStatusNum() {
        return this.statusNum;
    }
}
