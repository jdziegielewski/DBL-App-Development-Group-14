package com.dblgroup14.app.edit;

import android.view.View;
import com.dblgroup14.database_support.AppDatabase;
import com.dblgroup14.database_support.dao.EditDaoInterface;
import com.dblgroup14.database_support.entities.local.Challenge;
//class for editable challenges that are available to the user.
public abstract class EditChallengeFragment extends EditFragment<Challenge> {
    
    @Override
    protected void initialize(View view) {
        // Set page title
        //((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edit Challenge");
    }
    
    @Override
    protected boolean update() {
        // TODO: Update name
        return true;
    }
    
    @Override
    protected EditDaoInterface<Challenge> dao() {
        return AppDatabase.db().challengeDao();
    }
    
    @Override
    protected abstract Challenge createNew();
    
    @Override
    protected abstract int getLayoutResourceId();
}
