package com.mina.collegehelper.fragments;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.mina.collegehelper.R;

public class SettingsFragment extends PreferenceFragmentCompat {


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings);
    }

}
