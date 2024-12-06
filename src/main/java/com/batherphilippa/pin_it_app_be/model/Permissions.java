package com.batherphilippa.pin_it_app_be.model;

/**
 * Permissions - defines the permissions of project participants.
 */
public enum Permissions {

    OWNER("OWNER", 0, "Owner"),  // create, read, write, delete
    EDITOR_RWD("EDITOR_RWD", 1, "Editor with read, write, and delete permissions"), // read, write, delete tasks
    EDITOR_RW("EDITOR_RW", 2, "Editor with read and write permissions"), // read, write tasks
    VIEWER("READ_ONLY", 3, "Participant with read only permissions") ;// read

    private final String permissionsIdentifier;
    private final int permissionsNum;
    private final String permissionsDescription;

    Permissions(String permissionsIdentifier, int permissionsNum, String permissionsDescription) {
        this.permissionsIdentifier = permissionsIdentifier;
        this.permissionsNum = permissionsNum;
        this.permissionsDescription = permissionsDescription;
    }
    public String getPermissionsIdentifier() {
        return this.permissionsIdentifier;
    }

    public int getPermissionsNum() {
        return this.permissionsNum;
    }

    public String getPermissionsDescription() { return this.permissionsDescription; }

    public static Permissions setPermissionsByNum(int num) {
        return switch(num) {
            case 0 -> Permissions.OWNER;
            case 1 -> Permissions.EDITOR_RWD;
            case 2 -> Permissions.EDITOR_RW;
            default -> Permissions.VIEWER;
        };
    }
}
