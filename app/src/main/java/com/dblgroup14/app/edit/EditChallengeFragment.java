package com.dblgroup14.app.edit;

import android.view.View;
import com.dblgroup14.app.R;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.dao.HostDaoInterface;
import com.dblgroup14.support.entities.Challenge;

public class EditChallengeFragment extends EditFragment<Challenge> {
    
    @Override
    protected void initialize(View view) {
        // TODO: Implement
    }
    
    @Override
    protected boolean update() {
        // TODO: Implement
        return true;
    }
    
    @Override
    protected Challenge createNew() {
        // TODO: Implement
        return null;
    }
    
    @Override
    protected HostDaoInterface<Challenge> dao() {
        return AppDatabase.db().challengeDao();
    }
    
    @Override
    protected int getLayoutResourceID() {
        return R.layout.edit_challenges;
    }
}
