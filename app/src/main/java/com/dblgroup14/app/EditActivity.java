package com.dblgroup14.app;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.dblgroup14.app.edit.EditAlarmFragment;
import com.dblgroup14.app.edit.EditChallengeFragment;
import com.dblgroup14.app.edit.EditFragment;

public class EditActivity extends AppCompatActivity {
    private EditFragment editFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check if an object type has been provided
        if (!getIntent().hasExtra("object")) {
            throw new IllegalArgumentException("No object type given");
        }
        
        // Set lay-out
        setContentView(R.layout.activity_edit);
        
        // Create action bar
        createActionBarWithGradient();
        
        // Initialize edit fragment
        initializeEditFragment();
        
        // Add on click listener to save button
        findViewById(R.id.saveButton).setOnClickListener(v -> {
            if (editFragment.save()) {
                finish();
            }
        });
    }
    
    private void createActionBarWithGradient() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
    }
    
    private void initializeEditFragment() {
        // Instantiate edit fragment
        switch (getIntent().getStringExtra("object")) {
            case "alarm":
                editFragment = new EditAlarmFragment();
                break;
            case "challenge":
                // Get challenge class name
                String className = getIntent().getStringExtra("edit_fragment_class");
                if (className == null) {
                    throw new IllegalArgumentException("No edit fragment class given");
                }
                
                // Instantiate edit challenge fragment
                try {
                    editFragment = (EditChallengeFragment) Class.forName(className).newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException("No valid edit fragment class given");
                }
                break;
            default:
                break;
        }
    
        // Set arguments
        Bundle initArgs = new Bundle();
        initArgs.putInt("id", getIntent().getIntExtra("id", -1));
        editFragment.setArguments(initArgs);
        
        // Place fragment into container
        FragmentManager fragMgr = getSupportFragmentManager();
        fragMgr.beginTransaction().replace(R.id.editFragmentContainer, editFragment).commit();
    }
}
