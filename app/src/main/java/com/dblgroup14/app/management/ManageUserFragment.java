package com.dblgroup14.app.management;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.R;
import com.dblgroup14.support.SimpleDatabase;

public class ManageUserFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_user, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Instantiate correct child fragment
        updateChildFragment(SimpleDatabase.isLoggedIn());
    }
    
    /**
     * Instantiates the correct child fragment into this fragment's container.
     *
     * @param isLoggedIn Whether the app user is logged in
     */
    public void updateChildFragment(boolean isLoggedIn) {
        // Instantiate correct child fragment
        Fragment childFragment;
        if (isLoggedIn) {
            childFragment = new ManageUserProfileFragment();
        } else {
            childFragment = new ManageUserLoginFragment();
        }
        
        // Transact child fragment in current lay-out
        getChildFragmentManager().beginTransaction().replace(R.id.manageUserFragmentContainer, childFragment).commit();
    }
}
