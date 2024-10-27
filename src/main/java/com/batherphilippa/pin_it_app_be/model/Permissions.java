package com.batherphilippa.pin_it_app_be.model;

/**
 * Permissions - defines the permissions of project participants.
 */
public enum Permissions {

    OWNER,  // create, read, write, delete
    EDITOR, // read, write
    VIEWER // write
}
