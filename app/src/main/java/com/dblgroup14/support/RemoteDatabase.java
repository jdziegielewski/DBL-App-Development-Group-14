package com.dblgroup14.support;

import com.dblgroup14.support.entities.remote.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public abstract class RemoteDatabase {
    public static final String USERS_TABLE = "users";
    public static final String FRIENDS_TABLE = "user_friends";
    public static final String FRIEND_REQUESTS_TABLE = "friend_requests";
    public static final String PROFILE_PICTURES_FOLDER = "profile_pictures";
    
    /**
     * Returns the database reference to a 'table'.
     *
     * @param tableName The name of the table
     * @return A database reference
     */
    public static DatabaseReference getTableReference(String tableName) {
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
    
    /**
     * Gets the storage reference of a user's profile picture.
     *
     * @param user The user to get the profile picture reference for
     * @return The profile picture storage reference
     */
    public static StorageReference getProfilePictureReference(User user) {
        return FirebaseStorage.getInstance().getReference().child(PROFILE_PICTURES_FOLDER).child(user.profilePicture);
    }
}
