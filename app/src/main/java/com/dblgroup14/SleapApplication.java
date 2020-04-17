package com.dblgroup14;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import androidx.annotation.NonNull;
import com.dblgroup14.app.challenges.ShakeChallengeFragment;
import com.dblgroup14.database_support.AppDatabase;
import com.dblgroup14.database_support.RemoteDatabase;
import com.dblgroup14.database_support.dao.UserScoreDao;
import com.dblgroup14.database_support.entities.local.UserScore;
import com.dblgroup14.database_support.entities.remote.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SleapApplication extends Application {
    private static SleapApplication instance;
    
    private String currentUserUid;
    private List<UserScore> friendScores;
    private List<Pair<DatabaseReference, ValueEventListener>> eventListeners;
    public static String ShakeChallengeName;
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        
        // Instantiate database
        AppDatabase.createDatabase(getApplicationContext());
        
        // Get current app user's uid
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            registerRemoteEventListeners();
        }
        ShakeChallengeName = ShakeChallengeFragment.class.getSimpleName();
    }
    
    @Override
    public void onTerminate() {
        super.onTerminate();
        
        // Remove remote database event listeners
        if (eventListeners != null) {
            for (Pair<DatabaseReference, ValueEventListener> p : eventListeners) {
                p.first.removeEventListener(p.second);
            }
        }
        
        // Close database
        AppDatabase.closeDatabase();
        
        // Unset static instance
        instance = null;
    }
    
    public void registerRemoteEventListeners() {
        // Grab new user UID
        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        // Remove any existing remote database event listeners
        if (eventListeners != null) {
            for (Pair<DatabaseReference, ValueEventListener> p : eventListeners) {
                p.first.removeEventListener(p.second);
            }
        }
        
        // Create new event listeners list
        eventListeners = new ArrayList<>();
        
        // Register score data event listener
        DatabaseReference ref = RemoteDatabase.getTableReference(RemoteDatabase.SCORES_TABLE);
        ValueEventListener vel = ref.addValueEventListener(new SimpleValueEventListener(sd -> {
            RemoteDatabase.getTableReference(RemoteDatabase.FRIENDS_TABLE).child(currentUserUid)
                    .addListenerForSingleValueEvent(new SimpleValueEventListener(fd -> {
                        RemoteDatabase.getTableReference(RemoteDatabase.USERS_TABLE)
                                .addListenerForSingleValueEvent(new SimpleValueEventListener(ud -> {
                                    getFriendUserScores(sd, fd, ud);
                                }));
                    }));
        }));
        eventListeners.add(new Pair<>(ref, vel));
        
        // Register friend data event listener
        ref = RemoteDatabase.getTableReference(RemoteDatabase.FRIENDS_TABLE).child(currentUserUid);
        vel = ref.addValueEventListener(new SimpleValueEventListener(fd -> {
            RemoteDatabase.getTableReference(RemoteDatabase.SCORES_TABLE)
                    .addListenerForSingleValueEvent(new SimpleValueEventListener(sd -> {
                        RemoteDatabase.getTableReference(RemoteDatabase.USERS_TABLE)
                                .addListenerForSingleValueEvent(new SimpleValueEventListener(ud -> {
                                    getFriendUserScores(sd, fd, ud);
                                }));
                    }));
        }));
        eventListeners.add(new Pair<>(ref, vel));
        
        // Register user data event listener
        ref = RemoteDatabase.getTableReference(RemoteDatabase.USERS_TABLE);
        vel = ref.addValueEventListener(new SimpleValueEventListener(ud -> {
            RemoteDatabase.getTableReference(RemoteDatabase.FRIENDS_TABLE).child(currentUserUid)
                    .addListenerForSingleValueEvent(new SimpleValueEventListener(fd -> {
                        RemoteDatabase.getTableReference(RemoteDatabase.SCORES_TABLE)
                                .addListenerForSingleValueEvent(new SimpleValueEventListener(sd -> {
                                    getFriendUserScores(sd, fd, ud);
                                }));
                    }));
        }));
        eventListeners.add(new Pair<>(ref, vel));
    }
    
    /**
     * Generate a list of UserScore objects from the data snapshots of the scores, the user's friends and the user's data.
     *
     * @param scoresData  The data snapshot of the scores table
     * @param friendsData The data snapshot of the user's friends
     * @param usersData   The data snapshot of the user's table
     */
    private void getFriendUserScores(DataSnapshot scoresData, DataSnapshot friendsData, DataSnapshot usersData) {
        friendScores = new ArrayList<>();
        
        // Grab own user score
        UserScore ownScore = new UserScore();
        User ownUser = usersData.child(currentUserUid).getValue(User.class);
        ownScore.username = ownUser.username;
        if (scoresData.hasChild(currentUserUid)) {
            ownScore.score = scoresData.child(currentUserUid).getValue(Long.class);
        }
        friendScores.add(ownScore);
        
        // Iterate over all user friends
        for (DataSnapshot friendData : friendsData.getChildren()) {
            // Get friend UID
            String friendUid = friendData.getKey();
            if (friendUid == null) {
                continue;
            }
            
            // Create new UserScore object
            UserScore friendScore = new UserScore();
            
            // Set username
            if (usersData.hasChild(friendUid)) {
                User friend = usersData.child(friendUid).getValue(User.class);
                if (friend != null) {
                    friendScore.username = friend.username;
                }
            }
            
            // Set score
            if (scoresData.hasChild(friendUid)) {
                Long score = scoresData.child(friendUid).getValue(Long.class);
                if (score != null) {
                    friendScore.score = score;
                }
            }
            
            // Add object to list
            friendScores.add(friendScore);
        }
        
        // Synchronize friend scores with local database
        AsyncTask.execute(this::synchronizeFriendScores);
    }
    
    /**
     * Synchronizes the local database UserScore objects based on the remote database data.
     * NOTE: This method should not be run on the main thread!
     */
    private void synchronizeFriendScores() {
        // Delete all locally stored user scores
        UserScoreDao userScoreDao = AppDatabase.db().userScoreDao();
        userScoreDao.deleteAll();
        
        // Store all user scores
        userScoreDao.storeAll(friendScores);
    }
    
    /**
     * Gets the application context.
     *
     * @return The application context
     */
    public static Context getContext() {
        return instance.getApplicationContext();
    }
    
    /**
     * Gets the application instance.
     *
     * @return The application instance
     */
    public static SleapApplication getInstance() {
        return instance;
    }
    
    /**
     * Simple interface to accept a data snapshot.
     */
    private interface SimpleValueEvent {
        void accept(DataSnapshot dataSnapshot);
    }
    
    /**
     * Simple value event listener class.
     */
    private static class SimpleValueEventListener implements ValueEventListener {
        private final SimpleValueEvent sevInterface;
        
        private SimpleValueEventListener(@NonNull SimpleValueEvent sevInterface) {
            this.sevInterface = sevInterface;
        }
        
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // Call interface
            sevInterface.accept(dataSnapshot);
        }
        
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    }
}
