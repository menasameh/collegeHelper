package com.mina.collegehelper.model;

/**
 * Created by mina on 20/10/17.
 */

public class ServerResponse {

    public boolean success;
    public Object data;
    public String error;

    private ServerResponse(boolean success, Object data, String error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    static ServerResponse success(Object data) {
        return new ServerResponse(true, data, null);
    }

    static ServerResponse error(String error) {
        return new ServerResponse(false, null, error);
    }
}
