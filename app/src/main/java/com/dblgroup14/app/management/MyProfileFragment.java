package com.dblgroup14.app.management;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.R;
import com.dblgroup14.database_support.RemoteDatabase;
import com.dblgroup14.database_support.entities.remote.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MyProfileFragment extends Fragment {
    private TextView usernameText;
    private TextView emailAddressText;
    private TextView phoneNumberText;
    private TextView emailNotVerifiedText;
    private Button verifyEmailBtn;
    
    private Pair<DatabaseReference, ValueEventListener> userDataVel;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mup_profile, container, false);
    }
    
    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get components
        usernameText = view.findViewById(R.id.profileUsername);
        emailAddressText = view.findViewById(R.id.profileEmailAddress);
        phoneNumberText = view.findViewById(R.id.profilePhoneNumber);
        emailNotVerifiedText = view.findViewById(R.id.emailNotVerifiedText);
        verifyEmailBtn = view.findViewById(R.id.verifyEmailBtn);
        
        // Set click listener for verify email button
        verifyEmailBtn.setOnClickListener(v -> sendEmailVerification());
        
        // Set initial UI state
        setInitialUiState();
        
        // Register event handler to update user data
        registerUserDataListener();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        // Deregister event handler
        if (userDataVel != null) {
            userDataVel.first.removeEventListener(userDataVel.second);
        }
    }
    
    /**
     * Sets the UI to an initial state in which everything is indicated as being loaded.
     */
    private void setInitialUiState() {
        usernameText.setText("Loading...");
        emailAddressText.setText("Loading...");
        phoneNumberText.setText("Loading...");
        
        // Hide email not verified components
        emailNotVerifiedText.setVisibility(View.GONE);
        verifyEmailBtn.setVisibility(View.GONE);
    }
    
    /**
     * Registers an event handler that updates the user data it whenever it changes.
     */
    private void registerUserDataListener() {
        DatabaseReference ref = RemoteDatabase.getCurrentUserReference();
        ValueEventListener vel = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get user object value
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    Toast.makeText(getContext(), "Something went wrong while loading user data!", Toast.LENGTH_LONG).show();
                    return;
                }
                
                // Update user data
                updateUI(user);
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        
        // Set event handler pair
        userDataVel = new Pair<>(ref, vel);
    }
    
    /**
     * Updates / fills the UI with user data.
     *
     * @param user The user data
     */
    private void updateUI(User user) {
        // Get FirebaseAuth user instance
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        
        // Show / hide email verification UI elements
        if (authUser.isEmailVerified()) {
            emailNotVerifiedText.setVisibility(View.GONE);
            verifyEmailBtn.setVisibility(View.GONE);
        } else {
            emailNotVerifiedText.setVisibility(View.VISIBLE);
            verifyEmailBtn.setVisibility(View.VISIBLE);
        }
        
        // Update UI elements
        usernameText.setText(user.username);
        emailAddressText.setText(user.emailAddress);
        phoneNumberText.setText(user.phoneNumber);
    }
    
    /**
     * (Re)sends the a verification email to the user.
     */
    private void sendEmailVerification() {
        // Get FirebaseAuth user instance
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        
        // Send verification email
        authUser.sendEmailVerification().addOnCompleteListener(t -> {
            if (t.isSuccessful()) {
                Toast.makeText(getContext(), "Verification email has been sent!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "An error occurred while sending the verification email! " + t.getException().getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
