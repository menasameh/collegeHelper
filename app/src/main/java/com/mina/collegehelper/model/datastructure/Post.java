package com.mina.collegehelper.model.datastructure;

/**
 * Created by mina on 19/02/18.
 */

public class Post {

    public String id;

    public String teacherId;
    public String timestamp;
    public String text;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

}
