package com.dblgroup14.app.user;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.dblgroup14.app.R;
import com.dblgroup14.database_support.RemoteDatabase;
import com.dblgroup14.database_support.entities.remote.User;
import com.dblgroup14.database_support.entities.remote.UserFriend;
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
    
    /**
     * Initialize this recycler adapter with the app context, a query to fetch data from and whether this recycler view will be the search friends
     * list.
     *
     * @param context The application context
     * @param query A Firebase RemoteDatabase query that is the source for the data that is represented in this recycler view adapter
     * @param isSearchFriendList Whether this recycler view adapter will be attached to a recycler view in the search friends activity
     */
    public FriendsRecyclerAdapter(Context context, Query query, boolean isSearchFriendList) {
        super(UserFriend.class, R.layout.all_users_display_layout, FriendsViewHolder.class, query);
        
        this.context = context;
        this.userTableReference = RemoteDatabase.getTableReference(RemoteDatabase.USERS_TABLE);
        this.isSearchFriendList = isSearchFriendList;
    }
    
    /**
     * Populate the view holder by filling view holders with data from the database.
     *
     * @param viewHolder A FriendsViewHolder instance that will host one row of data
     * @param model A UserFriend instance that will contain the data about the relation of our app's user to the searched friend
     * @param pos The position (index) of the row in the recycler view
     */
    @Override
    protected void populateViewHolder(final FriendsViewHolder viewHolder, final UserFriend model, int pos) {
        // Get the friend UID
        final String friendUid = getRef(pos).getKey();
        
        // Fetch friend data from database
        userTableReference.child(friendUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the friend user data
                User friend = dataSnapshot.getValue(User.class);
                
                // Set view holder to display an error when the friend data could not be loaded
                if (friend == null) {
                    viewHolder.setError();
                    return;
                }
                
                // Get the friends profile picture from url
                if (friend.profilePicture != null && !friend.profilePicture.isEmpty()) {
                    // Get reference
                    StorageReference profilePicture = RemoteDatabase.getProfilePictureReference(friend);
                    
                    // Get profile picture url
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
        
        // Add on click listener to start PersonProfileActivity
        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PersonProfileActivity.class);
            intent.putExtra(PersonProfileActivity.ARG_USER_UID, friendUid);
            context.startActivity(intent);
        });
    }
}
