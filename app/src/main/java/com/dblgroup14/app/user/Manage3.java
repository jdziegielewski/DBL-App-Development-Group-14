package com.dblgroup14.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.dblgroup14.app.MainActivity;
import com.dblgroup14.app.R;
import com.dblgroup14.app.management.ManageUserFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Manage3 extends AppCompatActivity {
    
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_manage_user2);
        
        mAuth = FirebaseAuth.getInstance();
        
        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout); //search for drawer
        navigationView = (NavigationView) findViewById(R.id.navigation_view); //search for navigation
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header); //including navigation header
        
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                //create a method
                
                UserMenuSelector(item);
                return false;
            }
        });
    
    
    
    }
    
    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(Manage3.this, ManageUserFragment.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    
    private void SendUserToFindFriendsActivity()
    {
        Intent loginIntent = new Intent(Manage3.this, FriendsActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    
    private void SendUserToFriendsListActivity()
    {
        Intent friendsListIntent = new Intent(Manage3.this, FriendsListActivity.class);
        friendsListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(friendsListIntent);
        finish();
    }
    
    private void SendUserToFindProfileActivity()
    {
        Intent loginIntent = new Intent(Manage3.this, ProfileActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    
    private void SendUserToFindHomeActivity()
    {
        Intent loginIntent = new Intent(Manage3.this, MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    
    private void UserMenuSelector(MenuItem item) //let the user click on the button
    {
        switch (item.getItemId())
        {
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
