package com.dblgroup14.app.user;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.dblgroup14.app.MainActivity;
import com.dblgroup14.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends Fragment {
    private EditText mEmail, mPassword;
    private Button mLoginBtn;
    private TextView mCreateBtn, mForgotPass;
    private ProgressBar progressBar;
    private FirebaseAuth fAuth;
    private TextView mEyeToggle;
    private Activity activity;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_user_login, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        assert activity != null;
        
        mEmail = view.findViewById(R.id.email);
        mPassword = view.findViewById(R.id.password);
        progressBar = view.findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = view.findViewById(R.id.loginBtn);
        mCreateBtn = view.findViewById(R.id.createButton);
        mForgotPass = view.findViewById(R.id.ForgotPass);
        
        mEyeToggle = view.findViewById(R.id.EYE);
        mEyeToggle.setVisibility(View.GONE);
        mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mPassword.getText().length() > 0) {
                    mEyeToggle.setVisibility(View.VISIBLE);
                } else {
                    mEyeToggle.setVisibility(View.GONE);
                }
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            
            }
        });
        
        mEyeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEyeToggle.getText() == "SHOW") {
                    mEyeToggle.setText("HIDE");
                    mPassword.setInputType((InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD));
                    mPassword.setSelection(mPassword.length());
                    
                } else {
                    mEyeToggle.setText("SHOW");
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPassword.setSelection(mPassword.length());
                }
                
            }
        });
        
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }
                
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required");
                    return;
                }
                
                if (password.length() < 6) {
                    mPassword.setError("Password Must be greater or equal to 6 characters");
                }
                
                progressBar.setVisibility(View.VISIBLE);
                
                //authenticate the user
                
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            
                        } else {
                            Toast.makeText(getActivity(), "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                });
                
                
            }
        });
        
        
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                //for testing purpose:
                //startActivity(new Intent(getActivity(), ProfileActivity.class));
            }
        });
        
        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                EditText resetMail = new EditText((v.getContext()));
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter Your Email To Receive Link To Reset Password.");
                passwordResetDialog.setView(resetMail);
                
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get email and send reset link
                        
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(activity, "Resent Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                                
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Ups! Error! Resent link could not be sent!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                
                                
                            }
                        });
                    }
                });
                
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog
                        
                    }
                });
                
                passwordResetDialog.create().show();
            }
        });
        
    }
}