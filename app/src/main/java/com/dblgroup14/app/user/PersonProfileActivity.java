package com.dblgroup14.app.user;

import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.dblgroup14.app.R;
import com.dblgroup14.support.RemoteDatabase;
import com.dblgroup14.support.entities.remote.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonProfileActivity extends AppCompatActivity {
    public static final String ARG_USER_UID = "user_uid";
    
    private TextView usernameText;
    private TextView emailAddressText;
    private TextView statusText;
    private TextView phoneNumberText;
    private CircleImageView profilePictureView;
    private Button topBtn, bottomBtn;
    
    private User user;                          // the data of the user that is being shown
    private String shownUserUid;                // the UID of the user that is being shown
    private boolean isOwnProfile;               // whether the user that is being shown is our app's user
    private boolean isFriend;                   // whether the user that is being shown is a friend of our app's user
    private boolean hasPendingFriendRequest;    // whether the user that is being shown has sent a friend request to our app's user
    private boolean hasSentFriendRequest;       // whether the our app's user has sent a friend request to the user being shown
    private String appUserUid;                  // the UID of our app's user
    
    private List<Pair<DatabaseReference, ValueEventListener>> eventListeners = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        
        // Hide action bar and change top bar color
        if (Build.VERSION.SDK_INT >= 21) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
            
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        
        // Get components
        usernameText = findViewById(R.id.my_profile_username);
        emailAddressText = findViewById(R.id.my_profile_email);
        statusText = findViewById(R.id.my_profile_status);
        phoneNumberText = findViewById(R.id.my_profile_phoneNumber);
        profilePictureView = findViewById(R.id.my_profile_picture);
        topBtn = findViewById(R.id.person_send_friend_request_btn);
        bottomBtn = findViewById(R.id.person_decline_friend_request);
        
        // Hide action buttons
        topBtn.setVisibility(View.GONE);
        bottomBtn.setVisibility(View.GONE);
        
        // Get uid of user to show from arguments
        shownUserUid = getIntent().getStringExtra(ARG_USER_UID);
        if (shownUserUid == null) {
            throw new IllegalArgumentException("No user UID argument given!");
        }
        
        // Register user data event handler
        registerUserDataUpdates();
        
        // Get UID of current app user and check whether our app's user is viewing its own profile
        appUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        isOwnProfile = appUserUid.equals(shownUserUid);
        
        // Register friends data event handlers
        if (!isOwnProfile) {
            registerFriendsDataUpdates();
            registerFriendRequestDataUpdates();
        }
        
        // Set click listeners for top and bottom buttons
        topBtn.setOnClickListener(v -> topBtnClicked());
        bottomBtn.setOnClickListener(v -> bottomBtnClicked());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // Detach all event listeners
        for (Pair<DatabaseReference, ValueEventListener> p : eventListeners) {
            p.first.removeEventListener(p.second);
        }
    }
    
    /**
     * Register for user data updates for the user being shown.
     */
    private void registerUserDataUpdates() {
        DatabaseReference ref = RemoteDatabase.getUserReference(shownUserUid);
        ValueEventListener vel = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get user object from data
                User fetchedUser = dataSnapshot.getValue(User.class);
                if (fetchedUser == null) {
                    errorExitActivity();
                    return;
                }
                
                // Set user data
                user = fetchedUser;
                
                // Update user data in UI
                updateUserData();
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorExitActivity();
            }
        });
        
        // Add event listener to list
        eventListeners.add(new Pair<>(ref, vel));
    }
    
    /**
     * Register for friends data updates of the our app's user.
     */
    private void registerFriendsDataUpdates() {
        DatabaseReference ref = RemoteDatabase.getTableReference(RemoteDatabase.FRIENDS_TABLE).child(appUserUid);
        ValueEventListener vel = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Reset isFriend flag
                isFriend = false;
                
                // Set isFriend flag if a snapshot exists
                if (dataSnapshot.exists()) {
                    isFriend = dataSnapshot.hasChild(shownUserUid);
                }
                
                // Update buttons in the UI
                updateButtons();
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        
        // Add event listener to list
        eventListeners.add(new Pair<>(ref, vel));
    }
    
    /**
     * Register for sent friend request data updates.
     */
    private void registerFriendRequestDataUpdates() {
        DatabaseReference ref = RemoteDatabase.getTableReference(RemoteDatabase.FRIEND_REQUESTS_TABLE);
        ValueEventListener vel = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Reset flags
                hasPendingFriendRequest = false;
                hasSentFriendRequest = false;
                
                // Set flags if a snapshot exists
                if (dataSnapshot.exists()) {
                    hasPendingFriendRequest = dataSnapshot.hasChild(shownUserUid) && dataSnapshot.child(shownUserUid).hasChild(appUserUid);
                    hasSentFriendRequest = dataSnapshot.hasChild(appUserUid) && dataSnapshot.child(appUserUid).hasChild(shownUserUid);
                }
                
                // Update buttons in the UI
                updateButtons();
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        
        // Add event listener to list
        eventListeners.add(new Pair<>(ref, vel));
    }
    
    /**
     * Updates / fills the UI with newly fetched user data.
     */
    private void updateUserData() {
        // Set user data
        usernameText.setText(user.username);
        emailAddressText.setText(String.format("Email: %s", user.emailAddress));
        phoneNumberText.setText(String.format("Phone: %s", user.phoneNumber));
        statusText.setText(user.status);
        
        // Fetch user profile picture
        if (user.profilePicture != null) {
            StorageReference profilePictureReference = RemoteDatabase.getProfilePictureReference(user);
            profilePictureReference.getDownloadUrl().addOnCompleteListener(t -> {
                if (t.isSuccessful()) {
                    Glide.with(this).load(t.getResult()).into(profilePictureView);
                } else {
                    Toast.makeText(this, "Could not load user profile picture!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    
    /**
     * Update the state of the two buttons in the UI.
     */
    private void updateButtons() {
        // Don't do anything if our app's user is viewing its own profile
        if (isOwnProfile) {
            return;
        }
        
        // Update button texts and visibilities
        if (isFriend) {
            topBtn.setText("Unfriend this person");
            topBtn.setVisibility(View.VISIBLE);
            bottomBtn.setVisibility(View.GONE);
        } else if (hasPendingFriendRequest) {
            topBtn.setText("Accept friend request");
            bottomBtn.setText("Decline friend request");
            topBtn.setVisibility(View.VISIBLE);
            bottomBtn.setVisibility(View.VISIBLE);
        } else if (hasSentFriendRequest) {
            topBtn.setText("Cancel friend request");
            topBtn.setVisibility(View.VISIBLE);
            bottomBtn.setVisibility(View.GONE);
        } else {
            topBtn.setText("Send friend request");
            topBtn.setVisibility(View.VISIBLE);
            bottomBtn.setVisibility(View.GONE);
        }
    }
    
    /**
     * Called whenever the app user taps the top button in the UI.
     */
    private void topBtnClicked() {
        if (isFriend) {
            unfriend();
        } else if (hasPendingFriendRequest) {
            acceptFriendRequest();
        } else if (hasSentFriendRequest) {
            cancelFriendRequest();
        } else {
            sendFriendRequest();
        }
    }
    
    /**
     * Called whenever the app user taps the bottom button in the UI.
     */
    private void bottomBtnClicked() {
        // Decline friend request
        declineFriendRequest();
    }
    
    /**
     * Sends a friend request to the user that is shown.
     */
    private void sendFriendRequest() {
        // Put friend request entry in database
        RemoteDatabase.getTableReference(RemoteDatabase.FRIEND_REQUESTS_TABLE)
                .child(appUserUid).child(shownUserUid).child("type").setValue("sent");
    }
    
    /**
     * Cancels a friend request to the user that is shown.
     */
    private void cancelFriendRequest() {
        // Remove friend request entry in database
        RemoteDatabase.getTableReference(RemoteDatabase.FRIEND_REQUESTS_TABLE)
                .child(appUserUid).child(shownUserUid).removeValue();
    }
    
    /**
     * Accepts a pending friend request from the user that is shown to our app's user.
     */
    private void acceptFriendRequest() {
        // Get current date
        String formattedDate = new SimpleDateFormat("dd-MMMM-yyyy").format(Calendar.getInstance().getTime());
        
        // Add app user to friends list of user shown
        RemoteDatabase.getTableReference(RemoteDatabase.FRIENDS_TABLE)
                .child(shownUserUid).child(appUserUid).child("date").setValue(formattedDate);
        
        // Add user shown to friends list of app user
        RemoteDatabase.getTableReference(RemoteDatabase.FRIENDS_TABLE)
                .child(appUserUid).child(shownUserUid).child("date").setValue(formattedDate);
        
        // Delete pending friend request (same effect as declining friend request)
        declineFriendRequest();
    }
    
    /**
     * Declines a pending friend request from the user that is shown to our app's user.
     */
    private void declineFriendRequest() {
        // Remove friend request entry
        RemoteDatabase.getTableReference(RemoteDatabase.FRIEND_REQUESTS_TABLE)
                .child(shownUserUid).child(appUserUid).removeValue();
    }
    
    /**
     * Unfriends the user that is shown from the our app's user.
     */
    private void unfriend() {
        // Remove app user as friend from user shown
        RemoteDatabase.getTableReference(RemoteDatabase.FRIENDS_TABLE)
                .child(shownUserUid).child(appUserUid).removeValue();
        
        // Remove user shown as friend from app user
        RemoteDatabase.getTableReference(RemoteDatabase.FRIENDS_TABLE)
                .child(appUserUid).child(shownUserUid).removeValue();
    }
    
    /**
     * Shows an error message to the user and finishes the activity.
     */
    private void errorExitActivity() {
        Toast.makeText(this, "Could not fetch user data!", Toast.LENGTH_LONG).show();
        finish();
    }
}
