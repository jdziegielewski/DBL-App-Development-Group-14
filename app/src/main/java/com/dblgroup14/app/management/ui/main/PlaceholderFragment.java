package com.dblgroup14.app.management.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.dblgroup14.app.R;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.entities.Challenge;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    
    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<Challenge> challengesList;
    ListView listView;
    TextView defaultChalView;
    
    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main2, container, false);
        
        LiveData<List<Challenge>> allChallenges = AppDatabase.db().challengeDao().all();
        allChallenges.observe(getViewLifecycleOwner(), l -> {
            challengesList = l;
            initializeChallengesLiveData();
        });
        
        return root;
    }
    
    private void initializeChallengesLiveData() {
        listView = getActivity().findViewById(R.id.listViewChallenges);
        defaultChalView = getActivity().findViewById(R.id.textViewDefChal);
    
        String[] challengeNames = new String[challengesList.size()];
        
        for (int i = 0; i < challengesList.size(); i++) {
            challengeNames[i] = (challengesList.get(i).name);
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, challengeNames);
        listView.setAdapter(adapter);
    }
    
}
