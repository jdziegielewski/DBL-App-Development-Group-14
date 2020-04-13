package com.dblgroup14.support.entities.remote;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Model for remote database user object.
 */
@IgnoreExtraProperties
public class User {
    public String username;
    public String emailAddress;
    public String phoneNumber;
    public String profilePicture;
    public String status;
}
