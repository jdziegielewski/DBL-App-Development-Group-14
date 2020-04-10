package com.dblgroup14.app.management;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.R;
import com.dblgroup14.app.user.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ManageUserLoginFragment extends Fragment {
    private EditText emailInput, passwordInput;
    private TextView showPasswordToggle;
    private ProgressBar loginProgressBar;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_user_login, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get components
        emailInput = view.findViewById(R.id.email);
        passwordInput = view.findViewById(R.id.password);
        showPasswordToggle = view.findViewById(R.id.EYE);
        loginProgressBar = view.findViewById(R.id.progressBar);
        
        Button loginBtn = view.findViewById(R.id.loginBtn);
        TextView forgotPasswordText = view.findViewById(R.id.ForgotPass);
        Button createAccountBtn = view.findViewById(R.id.createButton);
        
        // Hide password toggle
        showPasswordToggle.setVisibility(View.INVISIBLE);
        
        // Set event handlers
        setPasswordEventHandlers();
        loginBtn.setOnClickListener(v -> attemptLogin());
        forgotPasswordText.setOnClickListener(v -> forgotPassword());
        createAccountBtn.setOnClickListener(v -> startActivity(new Intent(getContext(), RegisterActivity.class)));
    }
    
    private void setPasswordEventHandlers() {
        // Set password text changed listener
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    showPasswordToggle.setVisibility(View.VISIBLE);
                } else {
                    showPasswordToggle.setVisibility(View.INVISIBLE);
                }
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Set password toggle click listener
        showPasswordToggle.setOnClickListener(v -> {
            int passInputType = passwordInput.getInputType();
            if ((passInputType & InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                showPasswordToggle.setText("SHOW");
                passInputType &= ~InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                passInputType |= InputType.TYPE_TEXT_VARIATION_PASSWORD;
            } else {
                showPasswordToggle.setText("HIDE");
                passInputType |= InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
            }
            
            // Set input type and selection
            passwordInput.setInputType(passInputType);
            passwordInput.setSelection(passwordInput.length());
        });
    }
    
    private void attemptLogin() {
        // Get email and password
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        
        // Validate email
        if (email.isEmpty()) {
            emailInput.setError("Email field is required");
            return;
        } else {
            emailInput.setError(null);
        }
        
        // Validate password
        if (password.isEmpty()) {
            passwordInput.setError("Password field is required");
            return;
        } else if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters long");
            return;
        } else {
            passwordInput.setError(null);
        }
        
        // Show progress bar
        loginProgressBar.setVisibility(View.VISIBLE);
        
        // Attempt Firebase authentication
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        // TODO: Store username in local database
                        
                        // Send signal to swap lay-outs to parent fragment (instance of ManageUserFragment)
                        ManageUserFragment parent = (ManageUserFragment) getParentFragment();
                        if (parent == null) {
                            throw new IllegalStateException("Parent fragment is not of type ManageUserFragment!");
                        }
                        parent.updateChildFragment(true);
                        
                        Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Could not log in! " + t.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    
                    // Hide progress bar
                    loginProgressBar.setVisibility(View.INVISIBLE);
                });
    }
    
    private void forgotPassword() {
        // Instantiate EditText to fill in email
        final EditText resetEmailInput = new EditText(getContext());
        resetEmailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        
        // Build request reset email dialog
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Reset password")
                .setMessage("Please enter your email address to receive a link to reset your password.")
                .setView(resetEmailInput)
                .setPositiveButton("Proceed", (d, w) -> {
                    sendPasswordResetEmail(resetEmailInput.getText().toString());
                })
                .setNegativeButton("Cancel", (d, w) -> {})
                .create();
        
        // Show dialog
        dialog.show();
    }
    
    private void sendPasswordResetEmail(String email) {
        // Send Firebase reset password email
        FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnSuccessListener(v -> {
                    Toast.makeText(getContext(), "A password reset email has been sent!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Could not send password reset email! " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
