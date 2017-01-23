package com.ergasia.omada5.WhiteRabbit.entities;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String displayName;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email,String  displayName) {
        this.username = username;
        this.email = email;
        this.displayName = displayName;
    }

}
// [END blog_user_class]
