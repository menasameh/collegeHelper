package com.mina.collegehelper.model.datastructure;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by mina on 20/01/18.
 */

public class Year implements Serializable {
    public String id;
    public String name;
    public Map<String, Boolean> courses;

    public Year() {

    }
}
