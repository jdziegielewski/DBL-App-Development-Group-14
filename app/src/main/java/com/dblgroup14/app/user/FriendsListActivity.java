package com.dblgroup14.app.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dblgroup14.Friends;
import com.dblgroup14.app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FriendsListActivity extends AppCompatActivity
{
    
    private RecyclerView myFriendList;
    private DatabaseReference FriendsRef, UserRef;
    private FirebaseAuth fAuth;
    private String online_user_id;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        
        fAuth = FirebaseAuth.getInstance();
        online_user_id = fAuth.getCurrentUser().getUid();
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(online_user_id);
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        
        
        myFriendList = (RecyclerView) findViewById(R.id.Friend_list);
        myFriendList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myFriendList.setLayoutManager(linearLayoutManager);
    
        DisplayAllFriends();
        
    }
    
    
    
    private void DisplayAllFriends()
    {
        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> firebaseRecycleAdapter
                = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>
                (
                        Friends.class,
                        R.layout.all_users_display_layout,
                        FriendsViewHolder.class,
                        FriendsRef
                        
                )
        {
            @Override
            protected void populateViewHolder(FriendsViewHolder viewHolder, Friends model, int position)
            {
                viewHolder.setDate(model.getDate());
                final String usersIDs = getRef(position).getKey();
                UserRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            final String userName = dataSnapshot.child("username").getValue().toString();
                            
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
    
    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        
        public FriendsViewHolder(View itemView)
        {
            super(itemView);
            
            mView = itemView;
            
        }
    
        public void setUsername(String username)
        {
            TextView myName = (TextView) mView.findViewById(R.id.All_users_name);
            myName.setText(username);
        
        }
    
        public void setDate(String date)
        {
            TextView friendsDate = (TextView) mView.findViewById(R.id.All_users_status);
            friendsDate.setText("Friends since: " + date);
        
        }
    }
    
    
}
