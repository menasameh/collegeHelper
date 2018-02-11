package com.mina.collegehelper.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.mina.collegehelper.R;
import com.mina.collegehelper.Utils;
import com.mina.collegehelper.adapters.CoursesListAdapter;
import com.mina.collegehelper.model.DatabaseHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.Course;
import com.mina.collegehelper.model.datastructure.ServerCallback;
import com.mina.collegehelper.model.datastructure.Year;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YearCourses extends BaseActivity {

    @BindView(R.id.coursesList)
    ListView coursesList;
    CoursesListAdapter coursesListAdapter;
    Year currentYear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.year_courses);
        ButterKnife.bind(this);
        getIntentValues();
        setupUI();
        loadCourses();
    }

    private void getIntentValues() {
        currentYear = (Year) getIntent().getSerializableExtra(Utils.Constants.YEAR);
    }

    private void setupUI() {
        setTitle(currentYear.name);
        coursesListAdapter = new CoursesListAdapter(this);
        coursesList.setAdapter(coursesListAdapter);
    }

    private void loadCourses() {
        DatabaseHelper.getYearCourses(currentYear, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if(response.success) {
                    coursesListAdapter.setList((ArrayList<Course>) response.data);
                    coursesListAdapter.notifyDataSetChanged();
                } else {
                    showToast(response.error);
                }
            }
        });
    }
}



