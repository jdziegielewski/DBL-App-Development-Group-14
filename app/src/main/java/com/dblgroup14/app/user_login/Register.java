package com.dblgroup14.app.user_login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.dblgroup14.app.MainActivity;
import com.dblgroup14.app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,mPhone,mRePassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    private TextView mEyeToggle;
    private TextView mEyeToggle2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        
        mFullName = findViewById(R.id.Full_Name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.createText);
        
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        
        mEyeToggle = findViewById(R.id.EYE);
        mEyeToggle.setVisibility(View.GONE);
        mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        
        
        
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
            }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mPassword.getText().length() > 0) {
                    mEyeToggle.setVisibility(View.VISIBLE);
                }
                else {
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
                if(mEyeToggle.getText() == "SHOW") {
                    mEyeToggle.setText("HIDE");
                    mPassword.setInputType((InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD));
                    mPassword.setSelection(mEyeToggle.length());
                    
                }else {
                    mEyeToggle.setText("SHOW");
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPassword.setSelection(mPassword.length());
                }
        
            }
        });
    
    
        mEyeToggle2 = findViewById(R.id.EYE2);
        mEyeToggle2.setVisibility(View.GONE);
        mRePassword = findViewById(R.id.password2);
        mRePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    
    
        mRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            
            }
        
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mRePassword.getText().length() > 0) {
                    mEyeToggle2.setVisibility(View.VISIBLE);
                }
                else {
                    mEyeToggle2.setVisibility(View.GONE);
                }
            
            }
        
            @Override
            public void afterTextChanged(Editable s) {
            
            }
        });
    
        mEyeToggle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEyeToggle2.getText() == "SHOW") {
                    mEyeToggle2.setText("HIDE");
                    mRePassword.setInputType((InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD));
                    mRePassword.setSelection(mEyeToggle2.length());
                
                }else {
                    mEyeToggle2.setText("SHOW");
                    mRePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mRePassword.setSelection(mRePassword.length());
                }
            
            }
        });
    
      
        
    
        
    
    
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fullName = mFullName.getText().toString();
                String phone = mPhone.getText().toString();
                String rePassword = mRePassword.getText().toString().trim();
                
    
                
                if (TextUtils.isEmpty(fullName)){
                    mFullName.setError("Name is Required");
                    return;
                }
    
                if(fullName.length() < 4 | fullName.length() > 12 ) {
                    mFullName.setError("Name should be between 4 to 12 characters");
                }
                
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    
                    return;
                }
    
                if (!password.equals(rePassword))
                {
                    mRePassword.setError("Passwords don't match");
                    return;
                }
                
                if(password.length() < 6 ) {
                    mPassword.setError("Password Must be greater or equal to 6 characters");
                }
    
                
                progressBar.setVisibility(View.VISIBLE);
                
                //start registering the user in our database
                
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            
                            //send verification link to email
                            
                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Register.this, "Verification Email Has Been Sent.", Toast.LENGTH_SHORT).show();
                                
        
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: Email not sent " + e.getMessage());
                                }
                            });
                            
                            
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for"+ userID);
        
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure: " + e.toString());
        
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            
                            
                        }else {
                            Toast.makeText(Register.this, "Error ! " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            
                        }
        
                    }
                });
                
                
    
    
    
            }
            
        });
        
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
    
            }
        });
        
    }
}
