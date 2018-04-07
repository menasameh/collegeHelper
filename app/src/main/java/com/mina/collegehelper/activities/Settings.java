package com.mina.collegehelper.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.mina.collegehelper.fragments.SettingsFragment;

public class Settings extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, new SettingsFragment()).commit();}
    }
}
