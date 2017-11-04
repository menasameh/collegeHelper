package com.mina.collegehelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mina.collegehelper.R;
import com.mina.collegehelper.model.AuthenticationHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends AppCompatActivity {

    @BindView(R.id.splashScreen) private View mContentView;


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
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
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
        startActivity(new Intent(this, Login.class));
    }

    private void goToHome() {
        startActivity(new Intent(this, Home.class));
    }
}