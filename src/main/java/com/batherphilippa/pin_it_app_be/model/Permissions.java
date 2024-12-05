package com.batherphilippa.pin_it_app_be.model;

/**
 * Permissions - defines the permissions of project participants.
 */
public enum Permissions {

    OWNER("OWNER_CRUD", 0),  // create, read, write, delete
    EDITOR_RWD("EDITOR_RW", 2), // read, write, delete tasks
    EDITOR_RW("EDITOR_RWD", 3), // read, write tasks
    VIEWER("VIEWER", 4) ;// read

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
            case 1 -> Permissions.EDITOR_RWD;
            case 2 -> Permissions.EDITOR_RW;
            default -> Permissions.VIEWER;
        };
    }
}
