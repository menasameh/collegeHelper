package com.mina.collegehelper.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.mina.collegehelper.R;
import com.mina.collegehelper.Validator;
import com.mina.collegehelper.model.AuthenticationHelper;
import com.mina.collegehelper.model.DatabaseHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.Code;
import com.mina.collegehelper.model.datastructure.ServerCallback;
import com.mina.collegehelper.model.datastructure.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Registration extends BaseActivity {

    private String CODE_NOT_VALID_FOR_EMAIL = "This code is for another email address";
    private String INVALID_NAME = "Invalid or empty Name";
    private String INVALID_EMAIL = "Invalid or empty Email";
    private String INVALID_PASSWORD = "Invalid or empty password";
    private String LOGIN_GENERAL_ERROR = "Can't Login,  try again later";
    private String INVALID_CODE = "Invalid code";
    private String USED_CODE = "This code was used before";

    private String IMAGE_UPLOADED = "Image uploaded successfully";
    private String IMAGE_UPLOAD_IN_PROGRESS = "Image upload in progress";

    @BindView(R.id.profilePicture) ImageButton profilePictureImage;
    @BindView(R.id.nameTextView) TextView nameTextView;
    @BindView(R.id.emailTextView) TextView emailTextView;
    @BindView(R.id.passwordTextView) TextView passwordTextView;
    @BindView(R.id.codeTextView) TextView codeTextView;

    @BindView(R.id.regButton) Button signUpButton;

    ProgressDialog dialog;
    private String name;
    private String email;
    private String password;
    private String code;

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
            checkCode();
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

    private void checkCode() {
        DatabaseHelper.checkCode(code, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if (response.success) {
                    Code code = (Code) response.data;
                    if(code.valid) {
                        if(code.email.equals(regUser.email)) {
                            regUser.type = code.type;
                            useCode();
                        } else {
                            dialog.hide();
                            dialog.dismiss();
                            showToast(CODE_NOT_VALID_FOR_EMAIL);
                        }
                    } else {
                        dialog.hide();
                        dialog.dismiss();
                        showToast(USED_CODE);
                    }
                } else {
                    dialog.hide();
                    dialog.dismiss();
                    showToast(response.error);
                }
            }
        });
    }

    private void useCode() {
        DatabaseHelper.useCode(code, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if (response.success) {
                    signUpUser();
                } else {
                    dialog.hide();
                    dialog.dismiss();
                    showToast(response.error);
                }
            }
        });
    }

    private void signUpUser() {
        AuthenticationHelper.signup(email, password, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if (response.success) {
                    regUser.id = ((FirebaseUser) response.data).getUid();
                    saveUserToDB(regUser);
                    AuthenticationHelper.sendEmailVerification();
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
                    goToLogin();
                } else {
                    showToast(response.error);
                }
            }
        });
    }

    private void goToLogin() {
        finish();
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

    private void collectParameters() {
        regUser = new User();
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        name = nameTextView.getText().toString();
        code = codeTextView.getText().toString();
//        email = "m@m.com";
//        password = "123456";
//        name = "mina";
//        code = "qwertyuiop";
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

        if (!Validator.validateCode(code)) {
            codeTextView.setError(INVALID_CODE);
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


