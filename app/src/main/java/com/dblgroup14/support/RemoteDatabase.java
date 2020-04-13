package com.dblgroup14.support;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class RemoteDatabase {
    private static final String USERS_TABLE = "users";
    private static final String FRIENDS_TABLE = "user_friends";
    private static final String FRIEND_REQUESTS_TABLE = "friend_requests";
    
    /**
     * Returns the database reference to a 'table'.
     *
     * @param tableName The name of the table
     * @return A database reference
     */
    private static DatabaseReference getTableReference(String tableName) {
        return FirebaseDatabase.getInstance().getReference().child(tableName);
    }
    
    /**
     * Gets a real-time database reference to the entry of the currently logged in user.
     *
     * @return A database entry or null if no user is logged in
     */
    public static DatabaseReference getCurrentUserReference() {
        // Get currently logged in user via FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return null;
        }
        
        // Get database entry
        return getTableReference(USERS_TABLE).child(currentUser.getUid());
    }
    
    /**
     * Gets the username of the currently logged in user.
     *
     * @return The current user's username or null if no user is logged in
     */
    public static String getCurrentUsername() {
        // Get database reference to the currently logged in user
        DatabaseReference reference = getCurrentUserReference();
        if (reference == null) {
            return null;
        }
        
        // Return the username of the current user
        return reference.child("username").toString();
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
