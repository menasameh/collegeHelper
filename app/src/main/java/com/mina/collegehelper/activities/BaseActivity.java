package com.mina.collegehelper.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by mina on 21/10/17.
 */

public class BaseActivity extends AppCompatActivity {

    <T> T getViewById(int id) {
        return (T) findViewById(id);
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
