package com.mina.collegehelper.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mina.collegehelper.model.datastructure.ServerCallback;

import static com.mina.collegehelper.model.AuthenticationHelper.authRef;

/**
 * Created by mina on 21/10/17.
 */

public class AuthenticationHelper {

    protected static FirebaseAuth authRef = FirebaseAuth.getInstance();

    public static String getCurrentUserId() {
        FirebaseUser user = authRef.getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }

    public static Boolean login(String email, String password, final ServerCallback response) {
        authRef.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new LoginCompleteHandler(response));
        return false;
    }

    public static Boolean signup() {
        return false;
    }

}


class LoginCompleteHandler implements OnCompleteListener<AuthResult> {

    ServerCallback response;

    public LoginCompleteHandler(ServerCallback response) {
        this.response = response;
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            FirebaseUser user = authRef.getCurrentUser();
            response.onFinish(ServerResponse.success(user));
        } else {
            response.onFinish(ServerResponse.error("Authentication failed."));
        }
    }

}