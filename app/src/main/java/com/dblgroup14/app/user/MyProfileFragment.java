package com.dblgroup14.app.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.dblgroup14.app.MainActivity;
import com.dblgroup14.app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileFragment extends Fragment {
    TextView fullName, email, phone, verifyMail;
    private FirebaseAuth fAuth;
    //FirebaseFirestore fStore;
    private String currentUserId;
    //String TAKE_IMAGE_URL = null;
    //int TAKE_IMAGE_CODE = 10001;
    Button resendCode;
    //ImageView ProfileImage;
    private DatabaseReference profileUserRef;
    
  
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Setup navigation tabs
    
        phone = view.findViewById(R.id.profilePhone);
        fullName = view.findViewById(R.id.profileName);
        email = view.findViewById(R.id.profileEmail);
    
        fAuth = FirebaseAuth.getInstance();
        //fStore = FirebaseFirestore.getInstance();
    
        currentUserId = fAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
        
    
        resendCode = view.findViewById(R.id.ErrVerifyBtn);
        verifyMail = view.findViewById(R.id.MsgVerifyErr);
    
        FirebaseUser user = fAuth.getCurrentUser();
    
        if (!user.isEmailVerified()) {
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
                            Log.d("tag", "onFailure: Email not sent " + e.getMessage());
                            Toast.makeText(v.getContext(), "Verification Email not sent! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                
                }
            });
        }
    
    
        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String myUserName = dataSnapshot.child("username").getValue().toString();
                    String myPhone = dataSnapshot.child("phone").getValue().toString();
                    String myEmail = dataSnapshot.child("email").getValue().toString();
                    String myProfileStatus = dataSnapshot.child("status").getValue().toString();
                
                    fullName.setText(myUserName);
                    email.setText(myEmail);
                    phone.setText(myPhone);
                }
            }
        
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    
        
    
    }
    
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
    
    
    
}
