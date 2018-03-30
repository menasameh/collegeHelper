package com.mina.collegehelper.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.mina.collegehelper.R;
import com.mina.collegehelper.Utils;
import com.mina.collegehelper.adapters.CoursesListAdapter;
import com.mina.collegehelper.adapters.PostsListAdapter;
import com.mina.collegehelper.model.AuthenticationHelper;
import com.mina.collegehelper.model.DatabaseHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.Course;
import com.mina.collegehelper.model.datastructure.Post;
import com.mina.collegehelper.model.datastructure.ServerCallback;
import com.mina.collegehelper.model.datastructure.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoursePosts extends BaseActivity {

    @BindView(R.id.postsList)
    ListView postsList;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    PostsListAdapter postsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_posts);
        ButterKnife.bind(this);
        setupUI();
        loadCourses();
    }

    private void setupUI() {
        String courseName = getIntent().getStringExtra(Utils.Constants.COURSE_NAME);
        setTitle(courseName);
        DatabaseHelper.getCurrentUser(new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if(response.success) {
                    User u = (User) response.data;
                    if(u.type.equals("doctor"))
                        fab.show();
                    else
                        fab.hide();
                } else {
                    fab.hide();
                }
            }
        });
        postsListAdapter = new PostsListAdapter(this);
        postsList.setAdapter(postsListAdapter);
    }

    private void loadCourses() {
        String courseId = getIntent().getStringExtra(Utils.Constants.COURSE_ID);
        DatabaseHelper.getCoursePosts(courseId, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if(response.success) {
                    postsListAdapter.setList((ArrayList<Post>) response.data);
                    postsListAdapter.notifyDataSetChanged();
                } else {
                    showToast(response.error);
                }
            }
        });
    }

    @OnClick(R.id.fab)
    public void navigateToNewPost() {
        startActivity(new Intent(this, Login.class));
    }

}


