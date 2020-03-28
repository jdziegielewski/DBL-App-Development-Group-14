package com.dblgroup14.app.management.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import com.dblgroup14.app.R;
import com.dblgroup14.app.edit.EditShakeChallengeFragment;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.entities.Challenge;
import java.util.List;

public class EditChallenges extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    
    private List<Challenge> challengesList;
    ListView listView;
    
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
        
        View root = inflater.inflate(R.layout.fragment_edit_challenges, container, false);
        
        LiveData<List<Challenge>> allChallenges = AppDatabase.db().challengeDao().all();
        allChallenges.observe(getViewLifecycleOwner(), l -> {
            challengesList = l;
            initializeChallengesLiveData(root);
        });
        return root;
    }
    
    private void initializeChallengesLiveData(View root) {
        listView = root.findViewById(R.id.listViewEditChallenges);
        
        String[] challengeNames = new String[challengesList.size()];
        
        for (int i = 0; i < challengesList.size(); i++) {
            challengeNames[i] = (challengesList.get(i).name);
        }
        
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, challengeNames);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position != 2) {
                Toast.makeText(getActivity(), "This challenge is not editable ", Toast.LENGTH_LONG).show();
            }
            if (position == 2) {
                EditShakeChallengeFragment fragment2 = new EditShakeChallengeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                Bundle arguments = new Bundle();
                fragment2.setArguments(arguments);
                ConstraintLayout fl = getActivity().findViewById(R.id.frameLayout2);
                fl.removeAllViews();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout2, fragment2);
                fragmentTransaction.commit();
            }
        });
        
    }
    
    
}
