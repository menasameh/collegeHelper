package com.mina.collegehelper.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.mina.collegehelper.R;
import com.mina.collegehelper.Validator;
import com.mina.collegehelper.model.AuthenticationHelper;
import com.mina.collegehelper.model.DatabaseHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.ServerCallback;
import com.mina.collegehelper.model.datastructure.User;

public class Registeration extends BaseActivity {

    private String INVALID_NAME = "Invalid or empty Name";
    private String INVALID_EMAIL = "Invalid or empty Email";
    private String INVALID_PASSWORD = "Invalid or empty password";
    private String LOGIN_GENERAL_ERROR = "Can't Login,  try again later";

    private ImageButton profilePictureImage;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView passwordTextView;
    private Button signUpButton;

    ProgressDialog dialog;

    private String name;
    private String email;
    private String password;
    private String profilePictureUrl;

    private User regUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeration);
        setupUI();
        setupHandlers();
    }

    private void setupUI() {
        profilePictureImage = getViewById(R.id.profilePicture);
        nameTextView = getViewById(R.id.nameTextView);
        emailTextView = getViewById(R.id.emailTextView);
        passwordTextView = getViewById(R.id.passwordTextView);
        signUpButton = getViewById(R.id.regButton);
    }

    private void setupHandlers() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Registeration.this, "", "Loading. Please wait...", true);
                collectParameters();
                if (validateParameters()) {
                    signUp();
                }
            }
        });
    }

    private void signUp() {
        AuthenticationHelper.signup(email, password, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if (response.success) {
                    regUser.id = ((FirebaseUser) response.data).getUid();
                    saveUserToDB(regUser);
                } else {
                    ShowToast(response.error);
                    dialog.hide();
                }
            }
        });
    }

    private void saveUserToDB(User regUser) {
        DatabaseHelper.saveUser(regUser, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if (response.success) {
                    goToLogin();
                } else {
                    ShowToast(response.error);
                }
                dialog.hide();
            }
        });
    }

    private void goToLogin() {
        finish();
    }

    private void collectParameters() {
        regUser = new User();
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        name = nameTextView.getText().toString();
        regUser.email = email;
        regUser.name = name;
    }

    private boolean validateParameters() {
        if (!Validator.validateName(name)) {
            nameTextView.setError(INVALID_NAME);
            return false;
        }

        if (!Validator.validateEmail(email)) {
            emailTextView.setError(INVALID_EMAIL);
            return false;
        }

        if (!Validator.validatePassword(password)) {
            passwordTextView.setError(INVALID_PASSWORD);
            return false;
        }
        return true;
    }
}
