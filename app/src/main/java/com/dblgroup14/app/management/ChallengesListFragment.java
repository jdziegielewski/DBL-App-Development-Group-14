package com.dblgroup14.app.management;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import com.dblgroup14.app.R;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.dao.ChallengeDao;
import com.dblgroup14.support.entities.Challenge;
import java.util.List;

public class ChallengesListFragment extends Fragment {
    public static final String ARGS_KEY_TYPE = "type";
    
    private boolean showDefaultsOnly;
    private List<Challenge> challengesList;
    private ArrayAdapter<String> challengesListAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenges_list, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Check if only default (built-in) challenges should be listed
        if (getArguments() == null || !getArguments().containsKey(ARGS_KEY_TYPE)) {
            throw new IllegalArgumentException("No type provided");
        }
        showDefaultsOnly = getArguments().getInt(ARGS_KEY_TYPE, 0) == 0;
        
        // Create array adapter
        challengesListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
        
        // Initialize challenges list view
        ListView challengeListView = view.findViewById(R.id.challengesListView);
        challengeListView.setAdapter(challengesListAdapter);
        challengeListView.setOnItemClickListener((parent, v, pos, id) -> {
            Challenge c = challengesList.get(pos);
            if (c.isEditable) {
                showManageDialog(c);
            }
        });
        
        // Register live data binding
        ChallengeDao dao = AppDatabase.db().challengeDao();
        LiveData<List<Challenge>> liveData = showDefaultsOnly ? dao.allDefault() : dao.allCustom();
        liveData.observe(getViewLifecycleOwner(), this::updateChallengeListAdapter);
    }
    
    private void updateChallengeListAdapter(List<Challenge> data) {
        challengesList = data;
        
        // Put challenge names in list adapter
        challengesListAdapter.clear();
        for (Challenge c : data) {
            challengesListAdapter.add(c.name);
        }
    }
    
    private void showManageDialog(final Challenge c) {
        // Compile list of choices
        String[] choices;
        if (showDefaultsOnly) {
            choices = new String[] {"Create own from template"};
        } else {
            choices = new String[] {"Edit", "Delete"};
        }
        
        // Create dialog
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Manage challenge")
                .setCancelable(true)
                .setItems(choices, (d, w) -> {
                    switch (w) {
                        case 0:
                            if (showDefaultsOnly) {
                                createFromTemplate(c);
                            } else {
                                editOwnChallenge(c);
                            }
                            break;
                        case 1:
                            deleteOwnChallenge(c);
                            break;
                        default:
                            break;
                    }
                })
                .create();
        dialog.show();
    }
    
    private void createFromTemplate(Challenge c) {
        // TODO: Implement
    }
    
    private void editOwnChallenge(Challenge c) {
        // TODO: Implement
    }
    
    private void deleteOwnChallenge(Challenge c) {
        // TODO: Implement
    }
}
