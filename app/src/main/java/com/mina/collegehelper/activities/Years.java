package com.mina.collegehelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.mina.collegehelper.R;
import com.mina.collegehelper.adapters.YearsListAdapter;
import com.mina.collegehelper.model.AuthenticationHelper;
import com.mina.collegehelper.model.DatabaseHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.ServerCallback;
import com.mina.collegehelper.model.datastructure.Year;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Years extends BaseActivity {

    @BindView(R.id.yearsList)
    ListView yearsList;
    YearsListAdapter yearsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.years);
        ButterKnife.bind(this);
        setupUI();
        loadYears();
    }

    private void setupUI() {
        yearsListAdapter = new YearsListAdapter(this);
        yearsList.setAdapter(yearsListAdapter);
    }


    private void loadYears() {
        DatabaseHelper.getYears(new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if(response.success) {
                    yearsListAdapter.setList((ArrayList<Year>) response.data);
                    yearsListAdapter.notifyDataSetChanged();
                } else {
                    showToast(response.error);
                }
            }
        });
    }

    @OnClick(R.id.signOut)
    public void signOut() {
        AuthenticationHelper.signOut();
        goToAuth();
    }

    @OnClick(R.id.settings)
    public void openSettings() {
        startActivity(new Intent(this, Settings.class));
    }


    @OnClick(R.id.userCourses)
    public void goToUserCourses() {
        Intent authIntent = new Intent(this, UserCourses.class);
        authIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(authIntent);
    }

    private void goToAuth() {
        Intent authIntent = new Intent(this, Login.class);
        authIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(authIntent);
    }

    public void onBackPressed(){}

}
