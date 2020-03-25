package com.dblgroup14.app.user_login;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class User extends AppCompatActivity {

    
    TextView fullName,email,phone,verifyMail;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    int TAKE_IMAGE_CODE = 10001;
    Button resendCode;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        ///use get activity for fragments
        setContentView(R.layout.activity_user);
        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        
        userId = fAuth.getCurrentUser().getUid();
        
        resendCode = findViewById(R.id.ErrVerifyBtn);
        verifyMail = findViewById(R.id.MsgVerifyErr);
        
        FirebaseUser user = fAuth.getCurrentUser();
        
        if(!user.isEmailVerified()) {
            verifyMail.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);
            
            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
    
                    
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Verification Email Has Been Sent.", Toast.LENGTH_SHORT).show();
            
            
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag","onFailure: Email not sent " + e.getMessage());
                            Toast.makeText(v.getContext(), "Verification Email not sent! " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        
                }
            });
        }
        
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d("TAG","Error:"+e.getMessage());
                    
                }
                else {
                    phone.setText(documentSnapshot.getString("phone"));
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));
                }
    
    
            }
        });
        
    }
    
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
    
    public void handleImageClick(View view) {
        
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent,TAKE_IMAGE_CODE);
        }
        
    }
    
    
}