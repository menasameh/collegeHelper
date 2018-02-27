package com.mina.collegehelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.mina.collegehelper.R;
import com.mina.collegehelper.adapters.CoursesListAdapter;
import com.mina.collegehelper.model.AuthenticationHelper;
import com.mina.collegehelper.model.DatabaseHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.Course;
import com.mina.collegehelper.model.datastructure.ServerCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCourses extends BaseActivity {

    @BindView(R.id.coursesList)
    ListView coursesList;
    CoursesListAdapter coursesListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_courses);
        ButterKnife.bind(this);
        setupUI();
        loadCourses();
    }

    private void setupUI() {
        coursesListAdapter = new CoursesListAdapter(this, false);
        coursesList.setAdapter(coursesListAdapter);
    }

    private void loadCourses() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseHelper.getUserCourses(currentUserId, new ServerCallback() {
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


    @OnClick(R.id.signOut)
    public void signOut() {
        AuthenticationHelper.signOut();
        goToAuth();
    }

    @OnClick(R.id.settings)
    public void openSettings() {
        startActivity(new Intent(this, Settings.class));
    }

    private void goToAuth() {
        Intent authIntent = new Intent(this, Login.class);
        authIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(authIntent);
    }

}
