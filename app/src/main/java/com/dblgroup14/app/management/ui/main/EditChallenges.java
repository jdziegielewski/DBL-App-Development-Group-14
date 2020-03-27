package com.dblgroup14.app.management.ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dblgroup14.app.MainActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.edit.EditShakeChallengeFragment;
import com.dblgroup14.app.management.ManageChallengesFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditChallenges extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    private Button createCustomShakeChallengeButton;
    
    public static EditChallenges newInstance(int index) {
        EditChallenges fragment = new EditChallenges();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }
    
    public EditChallenges() {
        // Required empty public constructor
    }
    
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createCustomShakeChallengeButton = getActivity().findViewById(R.id.add_shake_challenge_button);
        createCustomShakeChallengeButton.setOnClickListener(onEditShakeChallengeButtonClicked);
    }
    
    View.OnClickListener onEditShakeChallengeButtonClicked = v -> {
    
    
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_challenges, container, false);
    }
}
