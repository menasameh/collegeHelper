package com.mina.collegehelper.model.datastructure;

public class PostNotification {

    public String courseId;
    public String teacherId;
    public String text;

    public PostNotification() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
}
