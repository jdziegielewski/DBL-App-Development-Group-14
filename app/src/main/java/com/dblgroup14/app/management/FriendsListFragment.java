package com.dblgroup14.app.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dblgroup14.app.R;
import com.dblgroup14.app.user.FindFriendsActivity;
import com.dblgroup14.app.user.FriendsRecyclerAdapter;
import com.dblgroup14.support.RemoteDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class FriendsListFragment extends Fragment {
    private RecyclerView friendsRecyclerView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mup_friends, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get components
        friendsRecyclerView = view.findViewById(R.id.Friend_list);
        
        // Set layout for friends recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        friendsRecyclerView.setLayoutManager(linearLayoutManager);
        
        // Populate friends list
        populateFriendsList();
        
        // Set on click listener to add friends button
        view.findViewById(R.id.addFriend).setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), FindFriendsActivity.class));
        });
    }
    
    /**
     * Populate the friends list by creating and setting a new recycler view adapter.
     */
    private void populateFriendsList() {
        // Get current user uid
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        // Get database references
        DatabaseReference friendsTableReference = RemoteDatabase.getTableReference(RemoteDatabase.FRIENDS_TABLE).child(currentUserUid);
        
        // Set recycler view adapter
        friendsRecyclerView.setAdapter(new FriendsRecyclerAdapter(getContext(), friendsTableReference, false));
    }
}
