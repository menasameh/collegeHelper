package com.mina.collegehelper.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.mina.collegehelper.R;
import com.mina.collegehelper.adapters.YearsListAdapter;
import com.mina.collegehelper.model.DatabaseHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.ServerCallback;
import com.mina.collegehelper.model.datastructure.Year;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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
}
