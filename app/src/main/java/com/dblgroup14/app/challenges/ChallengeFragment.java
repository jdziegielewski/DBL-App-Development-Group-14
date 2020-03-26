package com.dblgroup14.app.challenges;

import android.os.AsyncTask;
import androidx.fragment.app.Fragment;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.dao.UserScoreDao;
import com.dblgroup14.support.entities.UserScore;

/**
 * The abstract superclass that hosts a challenge.
 */
public abstract class ChallengeFragment extends Fragment {
    private static final int CHALLENGE_WIN_POINT = 100;
    
    protected void completeChallenge() {
        // Update user score
        AsyncTask.execute(() -> {
            UserScoreDao dao = AppDatabase.db().userScoreDao();
            UserScore score = dao.getActiveUserScore();
            score.addPoints(CHALLENGE_WIN_POINT);
            dao.store(score);
        });
        
        getActivity().finish();
    }
    
    /**
     * Placeholder method intended to be overridden in challenge fragments that require onBackPressed() activity events.
     */
    public void onBackPressed() { }
}
