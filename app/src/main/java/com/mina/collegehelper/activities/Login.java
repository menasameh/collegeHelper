package com.mina.collegehelper.activities;

import android.app.ProgressDialog;
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
import com.mina.collegehelper.model.datastructure.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends BaseActivity {

    private String EMAIL_NOT_VERIFIED = "Please verify your email first";
    private String INVALID_EMAIL = "Invalid or empty Email";
    private String INVALID_PASSWORD = "Invalid or empty password";

    @BindView(R.id.emailTextView) TextView emailTextView;
    @BindView(R.id.passwordTextView) TextView passwordTextView;
    @BindView(R.id.loginButton) Button loginButton;
    @BindView(R.id.regButton) Button regButton;

    private ProgressDialog dialog;
    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.loginButton)
    void loginButtonAction() {
        collectParameters();
        if (validateParameters()) {
            dialog = ProgressDialog.show(Login.this, "", "Loading. Please wait...", true);
            login();
        }
    }

    @OnClick(R.id.regButton)
    void registrationButtonAction() {
        goToRegistration();
    }

    private void collectParameters() {
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
    }

    private boolean validateParameters() {
        if (!Validator.validateEmail(email)) {
            emailTextView.requestFocus();
            emailTextView.setError(INVALID_EMAIL);
            return false;
        }

        if (!Validator.validatePassword(password)) {
            passwordTextView.requestFocus();
            passwordTextView.setError(INVALID_PASSWORD);
            return false;
        }
        return true;
    }

    private void login() {
        AuthenticationHelper.login(email, password, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                dialog.hide();
                if (response.success) {
                    if (isEmailVerified()) {
                        goToHome();
                    }
                } else {
                    showToast(response.error);
                }
            }
        });
    }

    private boolean isEmailVerified() {
        if(!AuthenticationHelper.isEmailVerified()) {
            AuthenticationHelper.signOut();
            showToast(EMAIL_NOT_VERIFIED);
            return false;
        }
        return true;
    }

    private void goToHome() {
        Intent homeIntent = new Intent(Login.this, Home.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    private void goToRegistration() {
        startActivity(new Intent(Login.this, Registration.class));
    }
}