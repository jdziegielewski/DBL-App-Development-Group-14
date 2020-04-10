package com.dblgroup14.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.dblgroup14.app.MainActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.management.ManageUserFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Manage3 extends AppCompatActivity {
    
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    private TextView NavProfileUserName;
    private FirebaseAuth fAuth;
    private DatabaseReference UsersRef;
    
    String CurrentUserID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_manage_user2);
        
        mAuth = FirebaseAuth.getInstance();
        
        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout); //search for drawer
//        navigationView = (NavigationView) findViewById(R.id.navigation_view); //search for navigation
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header); //including navigation header
        
        fAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("users");
        CurrentUserID = fAuth.getCurrentUser().getUid();
        
        NavProfileUserName = (TextView) navView.findViewById(R.id.nav_user_user_name);
        
        UsersRef.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue().toString();
                    
                    NavProfileUserName.setText(username);
                }
                
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        });
        
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //create a method
                
                UserMenuSelector(item);
                return false;
            }
        });
        
        
    }
    
    
    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(Manage3.this, ManageUserFragment.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    
    private void SendUserToFindFriendsActivity() {
        Intent loginIntent = new Intent(Manage3.this, FriendsActivity.class);
        startActivity(loginIntent);
        
    }
    
    private void SendUserToFriendsListActivity() {
        Intent friendsListIntent = new Intent(Manage3.this, FriendsListFragment.class);
        startActivity(friendsListIntent);
        
    }
    
    private void SendUserToFindProfileActivity() {
        finish();
    }
    
    private void SendUserToFindHomeActivity() {
        Intent loginIntent = new Intent(Manage3.this, MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    
    private void UserMenuSelector(MenuItem item) //let the user click on the button
    {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                SendUserToFindProfileActivity();
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            
            case R.id.nav_home:
                SendUserToFindHomeActivity();
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            
            case R.id.nav_friends:
                SendUserToFriendsListActivity();
                Toast.makeText(this, "Friend list", Toast.LENGTH_SHORT).show();
                break;
            
            case R.id.nav_find_friends:
                SendUserToFindFriendsActivity();
                Toast.makeText(this, "Find Friends", Toast.LENGTH_SHORT).show();
                break;
            
            case R.id.nav_messages:
                Toast.makeText(this, "Messages", Toast.LENGTH_SHORT).show();
                break;
            
            case R.id.nav_Logout:
                mAuth.signOut();
                Toast.makeText(this, "Successfully Logout", Toast.LENGTH_SHORT).show();
                SendUserToLoginActivity();
                break;
        }
        
    }
    
    
}
