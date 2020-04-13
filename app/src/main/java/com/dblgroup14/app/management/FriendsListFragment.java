package com.dblgroup14.app.management;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.dblgroup14.app.R;
import com.dblgroup14.app.user.FriendsActivity;
import com.dblgroup14.support.RemoteDatabase;
import com.dblgroup14.support.entities.remote.Friend;
import com.dblgroup14.support.entities.remote.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import de.hdodenhof.circleimageview.CircleImageView;

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
            startActivity(new Intent(getActivity(), FriendsActivity.class));
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
        friendsRecyclerView.setAdapter(new FriendsRecyclerAdapter(getContext(), friendsTableReference));
    }
    
    /**
     * One entry in the friends list.
     */
    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView dateText;
        private CircleImageView profilePictureView;
        
        public FriendsViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.All_users_name);
            dateText = itemView.findViewById(R.id.All_users_status);
            profilePictureView = itemView.findViewById(R.id.All_users_image);
        }
        
        private void set(Context context, User friend, Friend relationModel, Uri profilePictureUri) {
            // Set name
            nameText.setText(friend.username);
            
            // Set date
            dateText.setText("Friends since: " + relationModel.date);
            
            // Set profile picture
            Glide.with(context).load(profilePictureUri).into(profilePictureView);
        }
        
        private void setError() {
            nameText.setText("ERROR");
            dateText.setText("ERROR");
        }
    }
    
    /**
     * The recycler view adapter for the friends list.
     */
    public static class FriendsRecyclerAdapter extends FirebaseRecyclerAdapter<Friend, FriendsViewHolder> {
        private final Context context;
        private final DatabaseReference userTableReference;
        
        private FriendsRecyclerAdapter(Context context, Query query) {
            super(Friend.class, R.layout.all_users_display_layout, FriendsViewHolder.class, query);
            this.context = context;
            this.userTableReference = RemoteDatabase.getTableReference(RemoteDatabase.USERS_TABLE);
        }
        
        @Override
        protected void populateViewHolder(final FriendsViewHolder viewHolder, final Friend model, int pos) {
            // Fetch friend data from database
            final String friendUid = getRef(pos).getKey();
            userTableReference.child(friendUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Get friend user data
                    User friend = dataSnapshot.getValue(User.class);
                    if (friend == null) {
                        viewHolder.setError();
                        return;
                    }
                    
                    // Get friends profile picture url
                    StorageReference profilePicture = RemoteDatabase.getProfilePictureReference(friend);
                    profilePicture.getDownloadUrl().addOnCompleteListener(t -> {
                        if (t.isSuccessful()) {
                            viewHolder.set(context, friend, model, t.getResult());
                        } else {
                            viewHolder.setError();
                        }
                    });
                }
                
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        }
    }
}
