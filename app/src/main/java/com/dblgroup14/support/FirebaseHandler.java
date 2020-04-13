package com.dblgroup14.support;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class FirebaseHandler {
    /**
     * Gets the username of the currently logged in user.
     *
     * @return The current user's username or null if no user is logged in
     */
    public static String getCurrentUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        } else {
            return user.getUid();
        }
    }
    
    /**
     * Checks whether the user is currently logged in into the Firebase database.
     *
     * @return Whether the user is logged in
     */
    public static boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
