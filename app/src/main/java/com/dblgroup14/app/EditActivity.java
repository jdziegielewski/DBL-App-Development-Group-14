package com.dblgroup14.app;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.dblgroup14.app.edit.EditAlarmFragment;
import com.dblgroup14.app.edit.EditChallengeFragment;
import com.dblgroup14.app.edit.EditFragment;

public class EditActivity extends AppCompatActivity {
    public static final String KEY_OBJECT_TYPE = "object_type";
    public static final String VAL_OBJECT_ALARM = "alarm";
    public static final String VAL_OBJECT_CHALLENGE = "challenge";
    public static final String KEY_OBJECT_ID = "id";
    public static final String KEY_EDIT_FRAGMENT_CLASS_NAME = "edit_fragment_class_name";
    
    private EditFragment editFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set lay-out
        setContentView(R.layout.activity_edit);
        
        // Initialize lay-out
        createActionBarWithGradient();
        initializeEditFragment();
        setSaveButton();
    }
    
    /**
     * Sets the layout with a gradient to the actionbar
     */
    private void createActionBarWithGradient() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient));
    }
    
    /**
     * Initializes the EditFragment, sets the arguments and places the fragment into the container
     */
    private void initializeEditFragment() {
        switch (getIntent().getStringExtra(KEY_OBJECT_TYPE)) {
            case VAL_OBJECT_ALARM:
                // Instantiate new EditAlarmFragment
                editFragment = new EditAlarmFragment();
                break;
            case VAL_OBJECT_CHALLENGE:
                // Get edit challenge fragment classname
                String className = getIntent().getStringExtra(KEY_EDIT_FRAGMENT_CLASS_NAME);
                if (className == null) {
                    throw new IllegalArgumentException("No edit fragment class given");
                }
                
                // Instantiate edit challenge fragment
                instantiateEditChallengeFragment(className);
                break;
            default:
                throw new IllegalArgumentException("Invalid object type given");
        }
        
        // Set edit fragment arguments
        Bundle initArgs = new Bundle();
        initArgs.putInt(KEY_OBJECT_ID, getIntent().getIntExtra(KEY_OBJECT_ID, -1));
        editFragment.setArguments(initArgs);
        
        // Place fragment into container
        FragmentManager fragMgr = getSupportFragmentManager();
        fragMgr.beginTransaction().replace(R.id.editFragmentContainer, editFragment).commit();
    }
    
    /**
     * Saves the editFragment and finishes the activity.
     */
    private void setSaveButton() {
        findViewById(R.id.saveButton).setOnClickListener(v -> {
            if (editFragment.save()) {
                finish();
            }
        });
    }
    
    /**
     * Instantiates the EditChallenge fragment
     *
     * @param className the name of the class of the challenge
     */
    private void instantiateEditChallengeFragment(String className) {
        try {
            editFragment = (EditChallengeFragment) Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("No valid edit fragment class given");
        }
    }
}
