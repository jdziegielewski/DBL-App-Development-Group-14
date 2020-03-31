package com.dblgroup14.app.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.dblgroup14.FindFriends;
import com.dblgroup14.app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FriendsActivity extends AppCompatActivity {
    
    private Toolbar mToolbar;
    private ImageButton SearchButton;
    private EditText SearchInputText;
    
    private RecyclerView SearchResultList;
    
    private DatabaseReference allUsersDatabaseRef;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        
        
        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users");
        
        SearchResultList = (RecyclerView) findViewById(R.id.Search_Results);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));
        
        
        SearchButton = (ImageButton) findViewById(R.id.Search_Friends_button);
        
        SearchInputText = (EditText) findViewById(R.id.Search_Box);
        
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String searchBoxInput = SearchInputText.getText().toString();
                
                SearchPeopleAndFriends(searchBoxInput);
        
            }
        });
        
        
        
        
       
    }
    
    private void SearchPeopleAndFriends(String searchBoxInput)
    {
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
                        
                )
        {
            
            @Override
            protected void populateViewHolder(FindFriendsViewHolder viewHolder, FindFriends model, int position)
            
            {
                viewHolder.setUsername(model.getUsername());
                viewHolder.setStatus(model.getStatus());
        
            }
    
           
        };
        
        SearchResultList.setAdapter(firebaseRecyclerAdapter);
        
    
    
    }
    
    
    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
    
        public FindFriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        
        //profile picture set
    
        public void setUsername(String username)
        {
            TextView myName = (TextView) mView.findViewById(R.id.All_users_name);
            myName.setText(username);
            
        }
    
        public void setStatus (String status)
        {
            TextView myStatus = (TextView) mView.findViewById(R.id.All_users_status);
            myStatus.setText(status);
        
        }
        
    }
}
