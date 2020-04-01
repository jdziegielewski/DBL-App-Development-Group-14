package com.dblgroup14.app.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.R;
import com.dblgroup14.app.user.LoginActivity;
import com.dblgroup14.app.user.Manage3;

public class ManageUserFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_user, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Set button click listeners
        setRedirectBtnClickListeners(view);
    }
    
    private void setRedirectBtnClickListeners(View view) {
        view.findViewById(R.id.MultiBtn).setOnClickListener(v -> {
            Intent challengesIntent = new Intent(getContext(), LoginActivity.class);
            startActivity(challengesIntent);
        });
        
        view.findViewById(R.id.ProfileBtn).setOnClickListener(v -> {
            Intent challengesIntent = new Intent(getContext(), Manage3.class);
            startActivity(challengesIntent);
        });
    }
}
