package com.mina.collegehelper.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.view.View;

import com.mina.collegehelper.R;
import com.mina.collegehelper.model.AuthenticationHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends AppCompatActivity {

    @BindView(R.id.splashScreen) public View mContentView;


    //time in milliseconds
    private static int SPLASH_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);
        ButterKnife.bind(this);
        setupUI();
        setupHandler();
    }

    private void setupUI() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void setupHandler() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(AuthenticationHelper.getCurrentUserId() != null) {
                    goToHome();
                } else {
                    goToAuth();
                }
            }
        }, SPLASH_DELAY);
    }

    private void goToAuth() {
        Intent authIntent = new Intent(this, Login.class);
        authIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(authIntent);
    }

    private void goToHome() {
        SharedPreferences shareprefs = PreferenceManager.getDefaultSharedPreferences(this);
        Resources resources = this.getResources();

        String screenIndex = shareprefs.getString("mainScreenChooser", "0");
        Class home = "0".equals(screenIndex) ? Years.class : UserCourses.class;
        Intent homeIntent = new Intent(this, home);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }
}