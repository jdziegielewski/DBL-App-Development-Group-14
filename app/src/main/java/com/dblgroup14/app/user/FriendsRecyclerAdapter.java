package com.dblgroup14.app.user;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.dblgroup14.app.R;
import com.dblgroup14.support.RemoteDatabase;
import com.dblgroup14.support.entities.remote.UserFriend;
import com.dblgroup14.support.entities.remote.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

/**
 * The recycler view adapter for a list of friends of the user of the app.
 */
public class FriendsRecyclerAdapter extends FirebaseRecyclerAdapter<UserFriend, FriendsViewHolder> {
    private final Context context;
    private final DatabaseReference userTableReference;
    private final boolean isSearchFriendList;
    
    public FriendsRecyclerAdapter(Context context, Query query, boolean isSearchFriendList) {
        super(UserFriend.class, R.layout.all_users_display_layout, FriendsViewHolder.class, query);
        
        this.context = context;
        this.userTableReference = RemoteDatabase.getTableReference(RemoteDatabase.USERS_TABLE);
        this.isSearchFriendList = isSearchFriendList;
    }
    //getting the user friends data
    @Override
    protected void populateViewHolder(final FriendsViewHolder viewHolder, final UserFriend model, int pos) {
        // Fetch the friend data from database
        final String friendUid = getRef(pos).getKey();
        userTableReference.child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the friend user data
                User friend = dataSnapshot.getValue(User.class);
                if (friend == null) {
                    viewHolder.setError();
                    return;
                }
                
                // Get the friends profile picture from url
                if (friend.profilePicture != null && !friend.profilePicture.isEmpty()) {
                    StorageReference profilePicture = RemoteDatabase.getProfilePictureReference(friend);
                    profilePicture.getDownloadUrl().addOnCompleteListener(t -> {
                        if (t.isSuccessful()) {
                            if (isSearchFriendList) {
                                viewHolder.setSearchFriend(context, friend, t.getResult());
                            } else {
                                viewHolder.setFriend(context, friend, model, t.getResult());
                            }
                        } else {
                            viewHolder.setError();
                        }
                    });
                } else if (isSearchFriendList) {
                    viewHolder.setSearchFriend(context, friend, null);
                } else {
                    viewHolder.setFriend(context, friend, model, null);
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        
        // Add on click listener
        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PersonProfileActivity.class);
            intent.putExtra(PersonProfileActivity.ARG_USER_UID, friendUid);
            context.startActivity(intent);
        });
    }
}
