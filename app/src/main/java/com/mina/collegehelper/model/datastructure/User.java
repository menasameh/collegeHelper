package com.mina.collegehelper.model.datastructure;

import java.util.Map;

/**
 * Created by mina on 20/10/17.
 */



public class User {

    public static enum userType{doctor, student};

    public String id;
    public String name;
    public String code;
    public String email;
    public String type;
    public String profilePictureUrl;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}