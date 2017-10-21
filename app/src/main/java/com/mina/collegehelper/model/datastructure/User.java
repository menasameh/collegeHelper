package com.mina.collegehelper.model.datastructure;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by mina on 20/10/17.
 */

public class User {

    public String name;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(User other){
        this.name = other.name;
        this.email = other.email;
    }
}