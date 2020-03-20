package com.dblgroup14.app.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.MainActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.user_login.Login;
import com.dblgroup14.app.user_login.Register;

public class ManageUserFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_user, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        view.findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent challengesIntent = new Intent(getContext(), Login.class);
                startActivity(challengesIntent);
            }
        });
        
    }
}
