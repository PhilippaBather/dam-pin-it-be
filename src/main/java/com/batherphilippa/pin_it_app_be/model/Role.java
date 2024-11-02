package com.batherphilippa.pin_it_app_be.model;

/**
 * Role - defines the permissions of project participants.
 */
public enum Role {

    OWNER,  // create, read, write, delete
    EDITOR, // read, write
    VIEWER // write
}
