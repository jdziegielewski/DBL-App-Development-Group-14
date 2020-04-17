package com.dblgroup14.database_support;

import com.dblgroup14.database_support.entities.remote.User;
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
    public static final String SCORES_TABLE = "scores";
    public static final String PROFILE_PICTURES_FOLDER = "profile_pictures";
    
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
        
        // Get database reference
        return getUserReference(currentUser.getUid());
    }
    
    /**
     * Gets a real-time database reference to score entry of the currently logged in user.
     *
     * @return A database reference or null if no user is logged in
     */
    public static DatabaseReference getCurrentUserScoreReference() {
        // Get currently logged in user via FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return null;
        }
        
        // Get database reference
        return getTableReference(SCORES_TABLE).child(currentUser.getUid());
    }
    
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
     * Gets the database reference to a user with the given uid.
     *
     * @param uid The uid of a user
     * @return The database reference to the request user
     */
    public static DatabaseReference getUserReference(String uid) {
        return getTableReference(USERS_TABLE).child(uid);
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
