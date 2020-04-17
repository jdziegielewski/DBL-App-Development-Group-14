package com.dblgroup14.app.user;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dblgroup14.app.R;
import com.dblgroup14.database_support.RemoteDatabase;
import com.google.firebase.database.Query;

public class FindFriendsActivity extends AppCompatActivity {
    private EditText searchInput;
    private RecyclerView searchResultsRecyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        
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
        searchInput = findViewById(R.id.Search_Box);
        searchResultsRecyclerView = findViewById(R.id.Search_Results);
        
        // Initialize search results recycler view
        searchResultsRecyclerView.setHasFixedSize(true);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Add click listener to search button
        findViewById(R.id.Search_Friends_button).setOnClickListener(v -> searchFriends());
    
        //Show the complete list of friends at the beginning before starting to search by username
        Query query = RemoteDatabase.getTableReference(RemoteDatabase.USERS_TABLE);
        searchResultsRecyclerView.setAdapter(new FriendsRecyclerAdapter(this, query, true));
    }
    
    /**
     * Searches for friends based on the app user's input.
     */
    private void searchFriends() {
        // Validate search keywords
        String searchKeywords = searchInput.getText().toString().trim();
        if (searchKeywords.isEmpty()) {
            return;
        }
        
        // Show message to the user
        Toast.makeText(this, "Searching...", Toast.LENGTH_LONG).show();
        
        // Compile search query
        Query query = RemoteDatabase.getTableReference(RemoteDatabase.USERS_TABLE)
                .orderByChild("username")
                .startAt(searchKeywords)
                .endAt(searchKeywords + "\uf8ff");
        
        // Set new recycler adapter
        searchResultsRecyclerView.setAdapter(new FriendsRecyclerAdapter(this, query, true));
    }
}
