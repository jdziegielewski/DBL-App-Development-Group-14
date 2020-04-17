package com.dblgroup14.app.management;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import com.dblgroup14.app.EditActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.database_support.AppDatabase;
import com.dblgroup14.database_support.dao.ChallengeDao;
import com.dblgroup14.database_support.entities.local.Challenge;
import java.util.List;

/**
 * Fragment class ChallengesListFragment to list the challenges available to the user in the app.
 * It initializes the view of the list of challenges in the ManageChallengeFragment.
 */
public class ChallengesListFragment extends Fragment {
    public static final String KEY_TYPE = "type";
    
    private boolean showDefaultsOnly;
    private List<Challenge> challengeList;
    private ArrayAdapter<String> challengeListAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_challenges_list, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Check if only default (built-in) challenges should be listed in the app
        if (getArguments() == null || !getArguments().containsKey(KEY_TYPE)) {
            throw new IllegalArgumentException("No type provided");
        }
        showDefaultsOnly = getArguments().getInt(KEY_TYPE, 0) == 0;
        
        // Create array adapter
        challengeListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
        
        // Initialize the challenges list view
        ListView challengeListView = view.findViewById(R.id.challengesListView);
        challengeListView.setAdapter(challengeListAdapter);
        challengeListView.setOnItemClickListener((parent, v, pos, id) -> {
            Challenge c = challengeList.get(pos);
            if (c.isEditable) {
                showManageDialog(c);
            } else {
                Toast.makeText(getContext(), "No options are available for this challenge", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Register a live data binding
        ChallengeDao dao = AppDatabase.db().challengeDao();
        LiveData<List<Challenge>> liveData = showDefaultsOnly ? dao.allDefault() : dao.allCustom();
        liveData.observe(getViewLifecycleOwner(), this::updateChallengeListAdapter);
    }
    
    /**
     * Update the challenge list adapter with the newly fetched challenge data.
     *
     * @param data The list of newly fetched challenge data
     */
    private void updateChallengeListAdapter(List<Challenge> data) {
        challengeList = data;
        
        // Put the challenge names in list adapter
        challengeListAdapter.clear();
        for (Challenge c : data) {
            challengeListAdapter.add(c.name);
        }
    }
    
    /**
     * Shows a dialog to the user containing options on how to manage a specific challenge in the list.
     *
     * @param c The challenge instance that is to be managed
     */
    private void showManageDialog(final Challenge c) {
        // Compile list of choices for the user
        String[] choices;
        if (showDefaultsOnly) {
            choices = new String[] {"Create own from template"};
        } else {
            choices = new String[] {"Edit"};
        }
        
        // Create dialog
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Manage challenge")
                .setCancelable(true)
                .setItems(choices, (d, w) -> {
                    if (w == 0) {
                        if (showDefaultsOnly) {
                            createFromTemplate(c);
                        } else {
                            editOwnChallenge(c);
                        }
                    }
                })
                .create();
        dialog.show();
    }
    
    /**
     * Starts the EditActivity to create a new challenge from a built-in template.
     *
     * @param c The challenge instance to be used as a template
     */
    private void createFromTemplate(Challenge c) {
        Intent intent = new Intent(getContext(), EditActivity.class);
        intent.putExtra(EditActivity.KEY_OBJECT_TYPE, EditActivity.VAL_OBJECT_CHALLENGE);
        intent.putExtra(EditActivity.KEY_EDIT_FRAGMENT_CLASS_NAME, c.getEditFragmentClassName());
        startActivity(intent);
    }
    
    /**
     * Starts the EditActivity to edit a challenge that was already created by the user.
     *
     * @param c The challenge instance that is to be edited
     */
    private void editOwnChallenge(Challenge c) {
        Intent intent = new Intent(getContext(), EditActivity.class);
        intent.putExtra(EditActivity.KEY_OBJECT_TYPE, EditActivity.VAL_OBJECT_CHALLENGE);
        intent.putExtra(EditActivity.KEY_OBJECT_ID, c.id);
        intent.putExtra(EditActivity.KEY_EDIT_FRAGMENT_CLASS_NAME, c.getEditFragmentClassName());
        startActivity(intent);
    }
}
