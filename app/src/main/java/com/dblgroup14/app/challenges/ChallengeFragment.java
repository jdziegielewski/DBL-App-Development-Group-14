package com.dblgroup14.app.challenges;

import androidx.fragment.app.Fragment;

/**
 * The abstract superclass that hosts a challenge.
 */
public abstract class ChallengeFragment extends Fragment {
    protected void completeChallenge() {
        // TODO: Implement
    }
    
    /**
     * Placeholder method intended to be overridden in challenge fragments that require onBackPressed() activity events.
     */
    public void onBackPressed() { }
}
