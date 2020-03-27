package com.dblgroup14.app.management.ui.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dblgroup14.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditChallenges extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    private PageViewModel pageViewModel;
    
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_challenges, container, false);
    }
}
