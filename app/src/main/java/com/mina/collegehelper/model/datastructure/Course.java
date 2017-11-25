package com.mina.collegehelper.model.datastructure;

/**
 * Created by mina on 09/11/17.
 */

public class Course {

    public String id;
    public String name;
    public String description;
    public String imageUrl;

    public Course() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}
