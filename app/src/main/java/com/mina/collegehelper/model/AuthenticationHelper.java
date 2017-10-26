package com.mina.collegehelper.model;

import android.drm.DrmErrorEvent;
import android.drm.DrmManagerClient;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    public static void login(String email, String password, final ServerCallback response) {
        authRef.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new LoginCompleteHandler(response))
                .addOnFailureListener(new LoginCompleteHandler(response));
    }

    public static void signup(String email, String password, final ServerCallback response) {
        authRef.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new SignUpCompleteHandler(response))
                .addOnFailureListener(new SignUpCompleteHandler(response));

    }

}


class LoginCompleteHandler implements OnSuccessListener<AuthResult>, OnFailureListener {

    private String LOGIN_GENERAL_ERROR = "Can't Login,  try again later";

    ServerCallback response;

    public LoginCompleteHandler(ServerCallback response) {
        this.response = response;
    }


    @Override
    public void onSuccess(AuthResult authResult) {
        FirebaseUser user = authRef.getCurrentUser();
        response.onFinish(ServerResponse.success(user));
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        response.onFinish(ServerResponse.error(e.getMessage()));
    }
}


class SignUpCompleteHandler implements OnSuccessListener<AuthResult>, OnFailureListener {

//    private String LOGIN_GENERAL_ERROR = "Can't Login,  try again later";

    ServerCallback response;

    public SignUpCompleteHandler(ServerCallback response) {
        this.response = response;
    }

    @Override
    public void onSuccess(AuthResult authResult) {
        FirebaseUser user = authRef.getCurrentUser();
        response.onFinish(ServerResponse.success(user));
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        response.onFinish(ServerResponse.error(e.getMessage()));
    }
}