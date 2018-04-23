package com.mina.collegehelper.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.mina.collegehelper.R;
import com.mina.collegehelper.Utils;
import com.mina.collegehelper.model.DatabaseHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.ServerCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPost extends BaseActivity {

    @BindView(R.id.post)
    EditText post;

    @BindView(R.id.send)
    Button addPost;

    ProgressDialog dialog;

    String courseId;
    String postId;
    String postText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        ButterKnife.bind(this);
        courseId = getIntent().getStringExtra(Utils.Constants.COURSE_ID);
        postId = getIntent().getStringExtra(Utils.Constants.POST_ID);
        postText = getIntent().getStringExtra(Utils.Constants.POST_TEXT);
        if(postId != null){
            addPost.setText("Edit");
            post.setText(postText);
        }
    }

    @OnClick(R.id.send)
    public void addNewPost() {
        String postText =  post.getText().toString();
        dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        //edit existing post
        if(postId != null){
            DatabaseHelper.editPost(courseId, postId, postText, new ServerCallback() {
                @Override
                public void onFinish(ServerResponse response) {
                    dialog.hide();
                    dialog.dismiss();
                    if(response.success){
                        finish();
                    } else {
                        showToast(response.error);
                    }
                }
            });
            return;
        }
        //add new post
        DatabaseHelper.addPost(courseId, postText, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                dialog.hide();
                dialog.dismiss();
                if(response.success){
                    finish();
                } else {
                    showToast(response.error);
                }
            }
        });
    }

}

