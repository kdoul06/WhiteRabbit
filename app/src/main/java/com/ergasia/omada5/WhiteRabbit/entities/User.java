package com.ergasia.omada5.WhiteRabbit.entities;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String displayName;
    public String surname;
    public String name;
    public String zip;
    public String phone;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String username, String email, String displayName, String surname, String name, String zip, String phone) {
        this.username = username;
        this.email = email;
        this.displayName = displayName;
        this.surname = surname;
        this.name = name;
        this.zip = zip;
        this.phone = phone;
    }
}
// [END blog_user_class]
