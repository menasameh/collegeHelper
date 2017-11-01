package com.mina.collegehelper.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Registration extends BaseActivity {

    private String INVALID_NAME = "Invalid or empty Name";
    private String INVALID_EMAIL = "Invalid or empty Email";
    private String INVALID_PASSWORD = "Invalid or empty password";
    private String LOGIN_GENERAL_ERROR = "Can't Login,  try again later";

    private String IMAGE_UPLOADED = "Image uploaded successfully";
    private String IMAGE_UPLOAD_IN_PROGRESS = "Image upload in progress";

    @BindView(R.id.profilePicture) ImageButton profilePictureImage;
    @BindView(R.id.nameTextView) TextView nameTextView;
    @BindView(R.id.emailTextView) TextView emailTextView;
    @BindView(R.id.passwordTextView) TextView passwordTextView;
    @BindView(R.id.regButton) Button signUpButton;

    ProgressDialog dialog;
    private String name;
    private String email;
    private String password;

    private Uri profilePictureUrl;
    private User regUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.regButton)
    void signUpButtonAction() {
        dialog = ProgressDialog.show(Registration.this, "", "Loading. Please wait...", true);
        collectParameters();
        if (validateParameters()) {
            signUp();
        } else {
            dialog.hide();
            dialog.dismiss();
        }
    }

    @OnClick(R.id.profilePicture)
    void chooseImageAction() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }

    private void signUp() {
        AuthenticationHelper.signup(email, password, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if (response.success) {
                    regUser.id = ((FirebaseUser) response.data).getUid();
                    saveUserToDB(regUser);
                } else {
                    showToast(response.error);
                    dialog.hide();
                    dialog.dismiss();
                }
            }
        });
    }

    private void saveUserToDB(final User regUser) {
        DatabaseHelper.saveUser(regUser, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                dialog.hide();
                dialog.dismiss();
                if (response.success) {
                    uploadImage();
                    goToHome();
                } else {
                    showToast(response.error);
                }
            }
        });
    }

    private void uploadImage() {

        if (profilePictureUrl == null) {
            return;
        }

        DatabaseHelper.saveImageToUser(regUser.id, profilePictureUrl, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if (response.success) {
                    showToast(IMAGE_UPLOADED);
                } else {
                    showToast(response.error);
                }
            }
        });
        showToast(IMAGE_UPLOAD_IN_PROGRESS);
    }

    private void goToHome() {
        Intent homeIntent = new Intent(Registration.this, Home.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
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

// return from image picker

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if(resultCode == RESULT_OK){
            profilePictureUrl = imageReturnedIntent.getData();
            profilePictureImage.setImageURI(profilePictureUrl);
        }
    }

}


