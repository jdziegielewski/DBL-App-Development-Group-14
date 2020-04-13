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
import android.widget.ImageView;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ManageUserProfileFragment extends Fragment {
    
    private ImageView ProfileImage;
    private StorageReference UserProfileImageRef;
    private String currentUserId;
    private FirebaseAuth fAuth;
    private DatabaseReference UserRef;
    
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
        
        // Initialize profile picture
        initializeProfilePicture(view);
        
        // Setup tabs
        ViewPager viewPager = view.findViewById(R.id.viewPagerProfile);
        TabLayout tabLayout = view.findViewById(R.id.tabLayoutProfile);
        TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new MyProfileFragment(), "Me");
        adapter.addFragment(new FriendsListFragment(), "My friends");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
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
        
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                StorageReference filePath = UserProfileImageRef.child(currentUserId + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Task<Uri> result1 = task.getResult().getMetadata().getReference().getDownloadUrl();
                        result1.addOnSuccessListener(uri -> {
                            final String downloadUrl = uri.toString();
                            UserRef.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Glide.with(getContext()).load(downloadUrl).into(ProfileImage);
                                            Toast.makeText(getContext(), "Profile image successfully uploaded.",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            String message = task1.getException().getMessage();
                                            Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                                        }
                                    });
                        });
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getContext(), "Error: Image cannot be cropped. Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void initializeProfilePicture(View view) {
        ProfileImage = (CircleImageView) view.findViewById(R.id.ProfileImage);
        fAuth = FirebaseAuth.getInstance();
        currentUserId = fAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        ProfileImage.setOnClickListener(v -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getActivity());
        });
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Glide.with(getContext()).load(image).into(ProfileImage);
                    } else {
                        Toast.makeText(getContext(), "Profile image do not exist in our Cloud...Please add an image by clicking the " +
                                "profile icon!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        
        // Send signal to swap lay-outs to parent fragment (instance of ManageUserFragment)
        ManageUserFragment parent = (ManageUserFragment) getParentFragment();
        if (parent == null) {
            throw new IllegalStateException("Parent fragment is not of type ManageUserFragment!");
        }
        parent.updateChildFragment(true);
    }
}
