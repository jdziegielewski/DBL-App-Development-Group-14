package com.dblgroup14.app.management;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.dblgroup14.app.R;
import com.dblgroup14.app.user.TabAdapter;
import com.dblgroup14.support.RemoteDatabase;
import com.dblgroup14.support.entities.remote.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ManageUserProfileFragment extends Fragment {
    private static final String PROFILE_PICTURE_FOLDER = "profile_pictures";
    
    private CircleImageView profileImageView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    
    private User currentUser;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_user_profile, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Hide action bar and change window colors
        if (Build.VERSION.SDK_INT >= 21) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.hide();
            
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        
        // Get components
        viewPager = view.findViewById(R.id.viewPagerProfile);
        tabLayout = view.findViewById(R.id.tabLayoutProfile);
        profileImageView = view.findViewById(R.id.userProfileImage);
        
        // Register logout button event handler
        view.findViewById(R.id.logoutBtn).setOnClickListener(v -> logout());
        
        // Register user data update listener
        registerUserUpdateListener();
        
        // Setup profile picture view
        setupProfilePictureView();
        
        // Setup profile tabs
        setupProfileTabs();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        // Show action bar and change window colors back
        if (Build.VERSION.SDK_INT >= 21) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.show();
            
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // Check for proper request code
        if (requestCode != CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            return;
        }
        
        // Check for proper result code
        if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Toast.makeText(getContext(), "Something went wrong when cropping the profile picture! Try again.", Toast.LENGTH_LONG).show();
            return;
        }
        
        // Set new profile picture
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (result != null) {
            setNewProfilePicture(result.getUri());
        }
    }
    
    /**
     * Registers an event handler that updates the user data it whenever it changes.
     */
    private void registerUserUpdateListener() {
        // Register value changed listener on user object
        RemoteDatabase.getCurrentUserReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get user object value
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(getContext(), "Something went wrong while loading the new user data!", Toast.LENGTH_LONG).show();
                    return;
                }
                
                // Set user data
                currentUser = user;
                
                // Load user profile picture
                loadProfilePicture();
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    
    /**
     * Sets up the image view that contains the user's profile picture.
     */
    private void setupProfilePictureView() {
        profileImageView.setOnClickListener(v -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getContext(), this);
        });
    }
    
    /**
     * Loads a new profile picture into the image view, based on a Firebase storage reference.
     */
    private void loadProfilePicture() {
        // Check if user has profile picture
        if (currentUser.profilePicture == null || currentUser.profilePicture.isEmpty()) {
            return;
        }
        
        // Create profile picture reference
        StorageReference reference = FirebaseStorage.getInstance().getReference().child(PROFILE_PICTURE_FOLDER).child(currentUser.profilePicture);
        
        // Obtain profile picture download url
        reference.getDownloadUrl().addOnCompleteListener(dlTask -> {
            if (dlTask.isSuccessful()) {
                Glide.with(getContext()).load(dlTask.getResult()).into(profileImageView);
            } else {
                Toast.makeText(getContext(), "Could not obtain URL for profile picture!", Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Uploads a new profile picture to the Firebase storage.
     *
     * @param newProfilePictureUri The uri of the new (local) profile picture file
     */
    private void setNewProfilePicture(Uri newProfilePictureUri) {
        // Get current user's uid
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        // Get file extension of newly selected picture
        String fileExtension = ".jpg";
        String path = newProfilePictureUri.getLastPathSegment();
        if (path != null && path.contains(".")) {
            fileExtension = path.substring(path.lastIndexOf("."));
        }
        
        // Get storage reference to the new file location
        final String fileName = String.format("%s%s", userUid, fileExtension);
        StorageReference reference = FirebaseStorage.getInstance()
                .getReference()
                .child(PROFILE_PICTURE_FOLDER)
                .child(fileName);
        
        // Upload new profile picture
        reference.putFile(newProfilePictureUri).continueWithTask(t -> {
            if (!t.isSuccessful()) {
                throw t.getException();
            }
            
            // Update profile picture data in user
            currentUser.profilePicture = fileName;
            return RemoteDatabase.getCurrentUserReference().setValue(currentUser);
        }).addOnFailureListener(uTask -> Toast.makeText(getContext(), "Could not upload new profile picture! Try again.", Toast.LENGTH_LONG).show());
    }
    
    /**
     * Sets up the navigation tabs in this fragment that allow the user to switch between profile information and friends list.
     */
    private void setupProfileTabs() {
        TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new MyProfileFragment(), "Me");
        adapter.addFragment(new FriendsListFragment(), "My friends");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    
    /**
     * Logs out the currently logged in user and switches manage user lay-out to the login screen.
     */
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        
        // Send signal to swap lay-outs to parent fragment (instance of ManageUserFragment)
        ManageUserFragment parent = (ManageUserFragment) getParentFragment();
        if (parent == null) {
            throw new IllegalStateException("Parent fragment is not of type ManageUserFragment!");
        }
        parent.updateChildFragment(false);
    }
}
