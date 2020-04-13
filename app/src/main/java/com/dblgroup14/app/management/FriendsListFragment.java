package com.dblgroup14.app.management;

import android.content.Context;
import android.content.Intent;
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
import com.dblgroup14.Friends;
import com.dblgroup14.app.R;
import com.dblgroup14.app.user.FriendsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsListFragment extends Fragment {
    
    private RecyclerView myFriendList;
    private DatabaseReference FriendsRef, UserRef;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mup_friends, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        FloatingActionButton addFriends = view.findViewById(R.id.addFriend);
        addFriends.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), FriendsActivity.class));
        });
    
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String online_user_id = fAuth.getCurrentUser().getUid();
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(online_user_id);
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        
        myFriendList = view.findViewById(R.id.Friend_list);
        //myFriendList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myFriendList.setLayoutManager(linearLayoutManager);
        
        DisplayAllFriends();
    }
    
    private void DisplayAllFriends() {
        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> firebaseRecycleAdapter
                = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>
                (
                        Friends.class,
                        R.layout.all_users_display_layout,
                        FriendsViewHolder.class,
                        FriendsRef
                
                ) {
            @Override
            protected void populateViewHolder(FriendsViewHolder viewHolder, Friends model, int position) {
                viewHolder.setDate(model.getDate());
                final String usersIDs = getRef(position).getKey();
                UserRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final String userName = dataSnapshot.child("username").getValue().toString();
                            
                            if (dataSnapshot.hasChild("profileimage")) {
                                final String profileImage = dataSnapshot.child("profileimage").getValue().toString();
                                viewHolder.setProfileimage(Objects.requireNonNull(getActivity()).getApplicationContext(), profileImage);
                            }
                            
                            
                            viewHolder.setUsername(userName);
                            
                            
                        }
                        
                    }
                    
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    
                    }
                });
                
                
            }
            
            
        };
        myFriendList.setAdapter(firebaseRecycleAdapter);
    }
    
    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        
        public FriendsViewHolder(View itemView) {
            super(itemView);
            
            mView = itemView;
            
        }
        
        public void setProfileimage(Context ctx, String profileimage) {
            CircleImageView myImage = (CircleImageView) mView.findViewById(R.id.All_users_image);
            Glide.with(ctx).load(profileimage).into(myImage);
            
        }
        
        public void setUsername(String username) {
            TextView myName = (TextView) mView.findViewById(R.id.All_users_name);
            myName.setText(username);
            
        }
        
        public void setDate(String date) {
            TextView friendsDate = (TextView) mView.findViewById(R.id.All_users_status);
            friendsDate.setText("Friends since: " + date);
            
        }
    }
    
    
}
