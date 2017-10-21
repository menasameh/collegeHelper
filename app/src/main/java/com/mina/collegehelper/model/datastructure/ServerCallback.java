package com.mina.collegehelper.model.datastructure;

import com.mina.collegehelper.model.ServerResponse;

/**
 * Created by mina on 20/10/17.
 */

public interface ServerCallback {
    void onFinish(ServerResponse response);
}
