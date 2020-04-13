package com.dblgroup14;

public class FindFriends
{
    
    public String profileimage, username,status;
    
    public FindFriends()
    {
    
    }
    
    public FindFriends(String profileimage, String username, String status)
    {
        this.profileimage = profileimage;
        this.username = username;
        this.status = status;
        
    }
    
    public String getProfileimage() {
        return profileimage;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
