package com.mina.collegehelper.model.datastructure;

import java.util.Map;

/**
 * Created by mina on 02/11/17.
 */

public class Code {
    public String type;
    public String email;
    public boolean valid;
    public Map<String, Boolean> courses;

    public Code() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}
