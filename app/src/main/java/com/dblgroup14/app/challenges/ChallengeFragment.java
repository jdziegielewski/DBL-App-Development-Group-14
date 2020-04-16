package com.dblgroup14.app.challenges;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.RemoteDatabase;
import com.dblgroup14.support.dao.UserScoreDao;
import com.dblgroup14.support.entities.local.UserScore;
import com.dblgroup14.support.entities.remote.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * The abstract superclass that hosts a challenge.
 */
public abstract class ChallengeFragment extends Fragment {
    private static final int CHALLENGE_WIN_POINT = 100;
    
    private OnChallengeCompletedListener challengeCompletedListener;
    
    protected void completeChallenge() {
        DatabaseReference userRef = RemoteDatabase.getCurrentUserReference();
        if (userRef != null) {
            // Grab username from remote database
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Update score
                    updateUserScore(dataSnapshot.getValue(User.class).username);
                }
                
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        } else {
            // Update local score only
            updateUserScore(null);
        }
        
        // Call challenge completed listener
        if (challengeCompletedListener != null) {
            challengeCompletedListener.onChallengeCompleted();
        }
    }
    
    /**
     * Updates the local and remote user score.
     *
     * @param username The username of the currently logged in user or null if no user is logged in
     */
    private void updateUserScore(final String username) {
        AsyncTask.execute(() -> {
            // Grab correct UserScore object from local database
            UserScoreDao dao = AppDatabase.db().userScoreDao();
            UserScore score;
            if (username == null) {
                score = dao.scoreOfUser("(default)");
            } else {
                score = dao.scoreOfUser(username);
            }
            
            // Update score locally
            score.addPoints(CHALLENGE_WIN_POINT);
            dao.store(score);
            
            // Update score remotely
            if (username != null) {
                RemoteDatabase.getCurrentUserScoreReference().setValue(score.score);
            }
        });
    }
    
    /**
     * Set a handler that is called when the challenge is completed.
     *
     * @param listener The handler to call
     */
    public void setOnChallengeCompletedListener(OnChallengeCompletedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Null listener given");
        }
        challengeCompletedListener = listener;
    }
    
    /**
     * Placeholder method intended to be overridden in challenge fragments that require onBackPressed() activity events.
     */
    public void onBackPressed() { }
    
    public interface OnChallengeCompletedListener {
        void onChallengeCompleted();
    }
}
