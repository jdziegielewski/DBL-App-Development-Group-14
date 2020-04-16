package com.dblgroup14.app.user;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.support.RemoteDatabase;
import com.dblgroup14.support.entities.remote.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText emailAddressInput;
    private EditText phoneNumberInput;
    private EditText passwordInput1;
    private EditText passwordInput2;
    private ProgressBar progressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
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
        usernameInput = findViewById(R.id.Full_Name);
        emailAddressInput = findViewById(R.id.email);
        phoneNumberInput = findViewById(R.id.phone);
        passwordInput1 = findViewById(R.id.password1);
        passwordInput2 = findViewById(R.id.password2);
        progressBar = findViewById(R.id.progressBar);
        TextView eyeToggle1 = findViewById(R.id.EYE);
        TextView eyeToggle2 = findViewById(R.id.EYE2);
        
        // Initialize components
        eyeToggle1.setVisibility(View.GONE);
        eyeToggle2.setVisibility(View.GONE);
        
        // Set password input fields event handlers
        setPasswordEventHandlers(passwordInput1, eyeToggle1);
        setPasswordEventHandlers(passwordInput2, eyeToggle2);
        
        // Set on click listener for register button
        findViewById(R.id.registerBtn).setOnClickListener(v -> validateRegistration());
        
        // Set on click listener for login text
        findViewById(R.id.loginText).setOnClickListener(v -> finish());
    }
    
    private void setPasswordEventHandlers(final EditText passwordInput, final TextView eyeToggle) {
        // Set password text changed listener
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    eyeToggle.setVisibility(View.VISIBLE);
                } else {
                    eyeToggle.setVisibility(View.GONE);
                }
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Set password toggle click listener
        eyeToggle.setOnClickListener(v -> {
            int passInputType = passwordInput.getInputType();
            if ((passInputType & InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                eyeToggle.setText("SHOW");
                passInputType &= ~InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                passInputType |= InputType.TYPE_TEXT_VARIATION_PASSWORD;
            } else {
                eyeToggle.setText("HIDE");
                passInputType |= InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
            }
            
            // Set input type and selection
            passwordInput.setInputType(passInputType);
            passwordInput.setSelection(passwordInput.length());
        });
    }
    
    /**
     * Validates the registration data for a new user.
     */
    private void validateRegistration() {
        final String password1 = passwordInput1.getText().toString().trim();
        final String password2 = passwordInput2.getText().toString().trim();
        
        // Validate password
        if (password1.isEmpty() || password2.isEmpty()) {
            passwordInput1.setError("Both passwords fields must be filled in");
            passwordInput2.setError("Both passwords fields must be filled in");
            return;
        } else if (password1.length() < 6) {
            passwordInput1.setError("Password must be of length 6 or longer");
            return;
        } else if (!password1.equals(password2)) {
            passwordInput1.setError("Passwords must match");
            passwordInput2.setError("Passwords must match");
            return;
        } else {
            passwordInput1.setError(null);
            passwordInput2.setError(null);
        }
        
        // Create new user object
        final User newUser = new User();
        newUser.username = usernameInput.getText().toString().trim();
        newUser.emailAddress = emailAddressInput.getText().toString().trim();
        newUser.phoneNumber = phoneNumberInput.getText().toString().trim();
        
        // Validate username input
        if (newUser.username.isEmpty()) {
            usernameInput.setError("Username is required");
            return;
        } else if (newUser.username.length() < 4 || newUser.username.length() > 12) {
            usernameInput.setError("Username length should be between 4 and 12 characters");
            return;
        } else {
            usernameInput.setError(null);
        }
        
        // Validate email input
        if (newUser.emailAddress.isEmpty()) {
            emailAddressInput.setError("Email is required");
            return;
        } else {
            emailAddressInput.setError(null);
        }
        
        // Check if user with certain username already exists
        Query query = RemoteDatabase.getTableReference(RemoteDatabase.USERS_TABLE).orderByChild("username").equalTo(newUser.username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    registerNewUser(newUser, password1);
                } else {
                    usernameInput.setError("Username is already taken");
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    
    /**
     * Registers a new user.
     *
     * @param newUser  A user object containing the new user's data
     * @param password The user's password
     */
    private void registerNewUser(User newUser, String password) {
        // Show progressbar
        progressBar.setVisibility(View.VISIBLE);
        
        // Register a new user in Firebase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(newUser.emailAddress, password).addOnCompleteListener(t -> {
            if (t.isSuccessful()) {
                // Send verification email
                sendVerificationEmail();
                
                // Register new user in database
                RemoteDatabase.getCurrentUserReference().setValue(newUser).addOnCompleteListener(t2 -> {
                    if (t2.isSuccessful()) {
                        // Hide progress bar
                        progressBar.setVisibility(View.INVISIBLE);
                        
                        // Show message
                        Toast.makeText(RegisterActivity.this, "Your account has been created!", Toast.LENGTH_LONG).show();
                        
                        // Finish activity
                        finish();
                    }
                });
            } else {
                // Hide progress bar
                progressBar.setVisibility(View.INVISIBLE);
                
                // Show error message
                Toast.makeText(RegisterActivity.this, "Could not register user! " + t.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Sends a verification email to the newly created user.
     */
    private void sendVerificationEmail() {
        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(t -> {
            if (t.isSuccessful()) {
                Toast.makeText(this, "Verification email has been sent!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Verification email could not be sent! Try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }
}