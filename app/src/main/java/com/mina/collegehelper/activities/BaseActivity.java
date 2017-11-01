package com.mina.collegehelper.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mina.collegehelper.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by mina on 21/10/17.
 */

public class BaseActivity extends AppCompatActivity {

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
