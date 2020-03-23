package com.dblgroup14.app.edit;

import android.view.View;
import android.widget.EditText;
import com.dblgroup14.app.R;
import com.dblgroup14.app.challenges.ShakeChallengeFragment;
import com.dblgroup14.support.entities.Challenge;

public class EditShakeChallengeFragment extends EditChallengeFragment {
    public static final String KEY_SHAKE_COUNT = "shake_count";
    
    private EditText shakeNumberInput;
    
    @Override
    protected void initialize(View view) {
        super.initialize(view);
        
        // Get shake number input
        shakeNumberInput = view.findViewById(R.id.UserShakesInput);
        shakeNumberInput.setText(String.valueOf(editObject.getInt(KEY_SHAKE_COUNT)));
    }
    
    @Override
    protected boolean update() {
        // Try to parse shake count
        int shakeCount;
        try {
            String t = shakeNumberInput.getText().toString().trim();
            if (t.isEmpty()) {
                return false;
            } else {
                shakeCount = Integer.parseInt(t);
            }
        } catch (Exception e) {
            return false;
        }
        
        // Set number of shakes
        editObject.put(KEY_SHAKE_COUNT, String.valueOf(shakeCount));
        
        return super.update();
    }
    
    @Override
    protected Challenge createNew() {
        Challenge shakeChallengeTemplate = new Challenge();
        shakeChallengeTemplate.className = ShakeChallengeFragment.class.getName();
        shakeChallengeTemplate.put(KEY_SHAKE_COUNT, "10");
        
        return shakeChallengeTemplate;
    }
    
    @Override
    protected int getLayoutResourceID() {
        return R.layout.fragment_edit_shake_challenge;
    }
}
