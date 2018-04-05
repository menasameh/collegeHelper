package com.mina.collegehelper.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.mina.collegehelper.R;
import com.mina.collegehelper.Utils;
import com.mina.collegehelper.model.DatabaseHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.ServerCallback;

public class AddPost extends BaseActivity {

    @BindView(R.id.post)
    EditText post;

    ProgressDialog dialog;

    String courseId;
    String postId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        ButterKnife.bind(this);
        courseId = getIntent().getStringExtra(Utils.Constants.COURSE_ID);
    }

    @OnClick(R.id.send)
    public void addNewPost() {
        String postText =  post.getText().toString();
        dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
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

