package com.dblgroup14.app.user;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.dblgroup14.app.R;
import com.dblgroup14.support.entities.remote.Friend;
import com.dblgroup14.support.entities.remote.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * One entry in a friends list.
 */
public class FriendsViewHolder extends RecyclerView.ViewHolder {
    private TextView nameText;
    private TextView secondaryText;
    private CircleImageView profilePictureView;
    
    public FriendsViewHolder(View itemView) {
        super(itemView);
        
        this.nameText = itemView.findViewById(R.id.All_users_name);
        this.secondaryText = itemView.findViewById(R.id.All_users_status);
        this.profilePictureView = itemView.findViewById(R.id.All_users_image);
    }
    
    public void setFriend(Context context, User friend, Friend relationModel, Uri profilePictureUri) {
        // Set name
        nameText.setText(friend.username);
        
        // Set date
        secondaryText.setText(String.format("Friends since: %s", relationModel.date));
        
        // Set profile picture
        if (profilePictureUri != null) {
            Glide.with(context).load(profilePictureUri).into(profilePictureView);
        }
    }
    
    public void setSearchFriend(Context context, User friend, Uri profilePictureUri) {
        // Set name
        nameText.setText(friend.username);
        
        // Set status
        secondaryText.setText(friend.status);
        
        // Set profile picture
        if (profilePictureUri != null) {
            Glide.with(context).load(profilePictureUri).into(profilePictureView);
        }
    }
    
    public void setError() {
        nameText.setText("ERROR");
        secondaryText.setText("ERROR");
    }
}
