package com.dblgroup14.app.user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.dblgroup14.FindFriends;
import com.dblgroup14.app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsActivity extends AppCompatActivity {
    
    private Toolbar mToolbar;
    private ImageButton SearchButton;
    private EditText SearchInputText;
    
    private RecyclerView SearchResultList;
    
    private DatabaseReference allUsersDatabaseRef;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        
        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");
        
        SearchResultList = (RecyclerView) findViewById(R.id.Search_Results);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));
        
        
        SearchButton = (ImageButton) findViewById(R.id.Search_Friends_button);
        
        SearchInputText = (EditText) findViewById(R.id.Search_Box);
        
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchBoxInput = SearchInputText.getText().toString();
                
                SearchPeopleAndFriends(searchBoxInput);
                
            }
        });
        
        
    }
    
    private void SearchPeopleAndFriends(String searchBoxInput) {
        Toast.makeText(this, "Searching....", Toast.LENGTH_LONG).show();
        Query searchPeopleandFriendsQuery = allUsersDatabaseRef.orderByChild("username")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");
        
        FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<FindFriends, FindFriendsViewHolder>
                (
                        FindFriends.class,
                        R.layout.all_users_display_layout,
                        FindFriendsViewHolder.class,
                        searchPeopleandFriendsQuery
                
                ) {
            
            @Override
            protected void populateViewHolder(FindFriendsViewHolder viewHolder, FindFriends model, int position) {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setProfileimage(getApplicationContext(), model.getProfileimage());
                
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String visit_user_id = getRef(position).getKey();
                        
                        Intent profileIntent = new Intent(FriendsActivity.this, PersonProfileActivity.class);
                        profileIntent.putExtra("visit_user_id", visit_user_id);
                        startActivity(profileIntent);
                        
                        
                    }
                });
                
            }
            
            
        };
        
        SearchResultList.setAdapter(firebaseRecyclerAdapter);
        
        
    }
    
    
    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        
        
        public FindFriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        
        public void setProfileimage(Context ctx, String profileimage) {
            CircleImageView myImage = (CircleImageView) mView.findViewById(R.id.All_users_image);
            Glide.with(ctx).load(profileimage).into(myImage);
            
        }
        
        public void setUsername(String username) {
            TextView myName = (TextView) mView.findViewById(R.id.All_users_name);
            myName.setText(username);
            
        }
        
        public void setStatus(String status) {
            TextView myStatus = (TextView) mView.findViewById(R.id.All_users_status);
            myStatus.setText(status);
            
        }
        
    }
}
