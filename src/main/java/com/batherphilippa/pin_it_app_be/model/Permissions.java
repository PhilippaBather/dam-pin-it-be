package com.batherphilippa.pin_it_app_be.model;

/**
 * Permissions - defines the permissions of project participants.
 */
public enum Permissions {

    OWNER("OWNER", 0),  // create, read, write, delete
    EDITOR("EDITOR", 1), // read, write, delete tasks
    VIEWER("VIEWER", 2) ;// write

    private final String permissionsName;
    private final int permissionsNum;

    Permissions(String permissionsName, int permissionsNum) {
        this.permissionsName = permissionsName;
        this.permissionsNum = permissionsNum;
    }

    public String getPermissionsName() {
        return this.permissionsName;
    }

    public int getPermissionsNum() {
        return this.permissionsNum;
    }

    public static Permissions setPermissionsByNum(int num) {
        return switch(num) {
            case 0 -> Permissions.OWNER;
            case 1 -> Permissions.EDITOR;
            default -> Permissions.VIEWER;
        };
    }
}
