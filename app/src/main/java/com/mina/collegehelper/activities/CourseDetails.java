package com.mina.collegehelper.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mina.collegehelper.R;
import com.mina.collegehelper.Utils;
import com.mina.collegehelper.adapters.UsersListAdapter;
import com.mina.collegehelper.model.DatabaseHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.Course;
import com.mina.collegehelper.model.datastructure.ServerCallback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetails extends BaseActivity {

    @BindView(R.id.name)
    TextView courseName;

    @BindView(R.id.image)
    ImageView courseImage;

    @BindView(R.id.description)
    TextView courseDescription;

    @BindView(R.id.doctorsList)
    ListView doctorsList;
    UsersListAdapter doctorsListAdapter;
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);
        ButterKnife.bind(this);
        setupUI();
        loadCourseDetails();
    }

    private void setupUI() {
        doctorsListAdapter = new UsersListAdapter(this);
        doctorsList.setAdapter(doctorsListAdapter);
    }

    private void loadCourseDetails() {

        String courseId = getIntent().getStringExtra(Utils.Constants.COURSE_ID);

        DatabaseHelper.getCourseDetails(courseId, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if(response.success) {
                    course = (Course) response.data;
                    bind(course);
                } else {
                    showToast(response.error);
                }
            }
        });
    }

    private void bind(Course course) {
        courseName.setText(course.name);
        courseDescription.setText(course.description);
        Picasso.with(this).load(course.imageUrl).into(courseImage);
    }

}
