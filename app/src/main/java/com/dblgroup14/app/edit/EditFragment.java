package com.dblgroup14.app.edit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.dblgroup14.support.dao.EditDaoInterface;

/**
 * The abstract superclass that hosts a fragment that shows or edits any type of object in this app.
 */
public abstract class EditFragment<T extends Object> extends Fragment {
    protected T editObject;     // the object instance that is being edited
    protected boolean isEdit;   // whether an existing object instance will be edited
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResourceId(), container, false);
    }
    
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Check whether arguments have been set
        if (getArguments() == null) {
            throw new IllegalArgumentException("No arguments set");
        }
        
        // Get supplied ID value
        int id = getArguments().getInt("id", -1);
        isEdit = (id >= 0);
        
        // Populate editObject
        if (isEdit) {
            AsyncTask.execute(() -> {
                editObject = dao().get(id);
                getActivity().runOnUiThread(() -> initialize(view));
            });
        } else {
            editObject = createNew();
            initialize(view);
        }
    }
    
    /**
     * Save the edited object in the database.
     *
     * @return Whether the save operation was successful
     */
    public boolean save() {
        // Update the object values
        if (!update()) {
            return false;
        }
        
        // Save this object in the database
        AsyncTask.execute(() -> {
            long rowId = dao().store(editObject);
            saveComplete(rowId);
        });
        
        return true;
    }
    
    /**
     * Callback for when the object has been saved.
     *
     * @param rowId The (new) row ID of the stored object
     */
    protected void saveComplete(long rowId) {}
    
    /**
     * Initializes the view.
     *
     * @param view The view parent
     */
    protected abstract void initialize(View view);
    
    /**
     * Updates the editObject's values to match the current values set in the edit lay-out.
     *
     * @return Whether the updated data is valid (i.e. no empty or invalid fields)
     */
    protected abstract boolean update();
    
    /**
     * Creates a new instance of the current object type.
     *
     * @return A new object instance
     */
    protected abstract T createNew();
    
    /**
     * Gets the DAO that belongs to this object type.
     *
     * @return DAO of this object type
     */
    protected abstract EditDaoInterface<T> dao();
    
    /**
     * Gets the layout resource ID that is used to edit this object.
     *
     * @return The layout resource ID
     */
    protected abstract int getLayoutResourceId();
}
