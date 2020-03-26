package com.dblgroup14.app;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.dblgroup14.app.edit.EditAlarmFragment;
import com.dblgroup14.app.edit.EditChallengeFragment;
import com.dblgroup14.app.edit.EditFragment;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    private EditFragment editFragment;
    
    /**
     * Check if an object type has been provided, sets the layout, action bar, initialize editFragment and sets the save button
     *
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getIntent().hasExtra("object")) {
            throw new IllegalArgumentException("No object type given");
        }
        setContentView(R.layout.activity_edit);
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
        switch (Objects.requireNonNull(getIntent().getStringExtra("object"))) {
            case "alarm":
                editFragment = new EditAlarmFragment();
                break;
            case "challenge":
                String className = getChallengeClass();
                instantiateEditChallengeFragment(className);
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
    
    /**
     * Gets the name of the class of the challenge
     *
     * @return the name of the class
     */
    private String getChallengeClass() {
        String className = getIntent().getStringExtra("edit_fragment_class");
        if (className == null) {
            throw new IllegalArgumentException("No edit fragment class given");
        }
        return className;
    }
    
    /**
     * Saves the editFragment and finished the activity
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
