package com.dblgroup14.app.edit;

import android.view.View;
import android.widget.NumberPicker;
import com.dblgroup14.app.R;
import com.dblgroup14.app.challenges.ShakeChallengeFragment;
import com.dblgroup14.support.entities.Challenge;

public class EditShakeChallengeFragment extends EditChallengeFragment {
    public static final String KEY_SHAKE_COUNT = "shake_count";
    private static final String[] SHAKE_AMOUNTS = {"5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70", "75", "80",
            "85", "90", "95", "100"};
    
    private NumberPicker shakeCountPicker;
    
    @Override
    protected void initialize(View view) {
        super.initialize(view);
        
        // Initialize shake count picker
        shakeCountPicker = view.findViewById(R.id.shakeCountPicker);
        shakeCountPicker.setMinValue(0);
        shakeCountPicker.setMaxValue(SHAKE_AMOUNTS.length - 1);
        shakeCountPicker.setValue(1);   // default 10 shakes
        shakeCountPicker.setDisplayedValues(SHAKE_AMOUNTS);
        shakeCountPicker.setWrapSelectorWheel(false);
        
        // Set correct index value of number picker
        int storedAmount = editObject.getInt(KEY_SHAKE_COUNT);
        for (int i = 0; i < SHAKE_AMOUNTS.length; i++) {
            if (Integer.parseInt(SHAKE_AMOUNTS[i]) == storedAmount) {
                shakeCountPicker.setValue(i);
                break;
            }
        }
    }
    
    @Override
    protected boolean update() {
        // Get actual shake count
        String actualShakes = SHAKE_AMOUNTS[shakeCountPicker.getValue()];
        
        // Set number of shakes
        editObject.put(KEY_SHAKE_COUNT, actualShakes);
        
        return super.update();
    }
    
    @Override
    protected Challenge createNew() {
        Challenge shakeChallengeTemplate = new Challenge();
        shakeChallengeTemplate.setName("Test");
        shakeChallengeTemplate.setClassName(ShakeChallengeFragment.class.getName());
        shakeChallengeTemplate.put(KEY_SHAKE_COUNT, SHAKE_AMOUNTS[1]);  // set 10 shakes as default
        return shakeChallengeTemplate;
    }
    
    @Override
    protected int getLayoutResourceID() {
        return R.layout.fragment_edit_shake_challenge;
    }
}
