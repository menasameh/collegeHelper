package com.mina.collegehelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mina.collegehelper.R;
import com.mina.collegehelper.Validator;
import com.mina.collegehelper.model.AuthenticationHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.ServerCallback;

public class Login extends BaseActivity {

    private String INVALID_EMAIL = "Invalid or empty Email";
    private String INVALID_PASSWORD = "Invalid or empty password";
    private String LOGIN_GENERAL_ERROR = "Can't Login,  try again later";

    private TextView emailTextView;
    private TextView passwordTextView;
    private Button loginButton;
    private Button regButton;

    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setupUI();
        setupHandlers();
    }

    private void setupUI() {
        emailTextView = getViewById(R.id.emailTextView);
        passwordTextView = getViewById(R.id.passwordTextView);
        loginButton = getViewById(R.id.loginButton);
        regButton = getViewById(R.id.regButton);
    }

    private void setupHandlers() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectParameters();
                if (validateParameters()) {
                    login();
                }
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegistration();
            }
        });

    }

    private void collectParameters() {
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
    }

    private boolean validateParameters() {
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

    private void login() {
        AuthenticationHelper.login(email, password, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if (response.success) {
                    goToHome();
                } else {
                    ShowToast(LOGIN_GENERAL_ERROR);
                }
            }
        });
    }

    private void goToHome() {
        startActivity(new Intent(Login.this, Home.class));
        finish();
    }

    private void goToRegistration() {
        startActivity(new Intent(Login.this, Registration.class));
    }
}