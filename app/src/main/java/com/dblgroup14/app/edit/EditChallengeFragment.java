package com.dblgroup14.app.edit;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.dao.HostDaoInterface;
import com.dblgroup14.support.entities.Challenge;

public abstract class EditChallengeFragment extends EditFragment<Challenge> {
    
    @Override
    protected void initialize(View view) {
        // Set page title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edit Challenge");
    }
    
    @Override
    protected boolean update() {
        // TODO: Update name
        return true;
    }
    
    @Override
    protected abstract Challenge createNew();
    
    @Override
    protected abstract int getLayoutResourceID();
    
    @Override
    protected HostDaoInterface<Challenge> dao() {
        return AppDatabase.db().challengeDao();
    }
}
