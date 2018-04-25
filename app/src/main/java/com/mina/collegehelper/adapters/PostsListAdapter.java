package com.mina.collegehelper.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mina.collegehelper.R;
import com.mina.collegehelper.Utils;
import com.mina.collegehelper.activities.AddPost;
import com.mina.collegehelper.model.DatabaseHelper;
import com.mina.collegehelper.model.ServerResponse;
import com.mina.collegehelper.model.datastructure.Post;
import com.mina.collegehelper.model.datastructure.ServerCallback;
import com.mina.collegehelper.model.datastructure.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mina on 09/11/17.
 */

public class PostsListAdapter extends BaseAdapter {

    private String courseId;
    private boolean isDoctor;
    private ArrayList<Post> list;
    private Context context;


    public PostsListAdapter(Context context, ArrayList<Post> list) {
        this.list = list;
        this.context = context;
    }

    public PostsListAdapter(Context context, String courseId, boolean isDoctor) {
        this.list = new ArrayList();
        this.context = context;
        this.courseId = courseId;
        this.isDoctor = isDoctor;
    }

    public void setList(ArrayList<Post> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Post getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        ButterKnife.setDebug(true);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        final Post item = getItem(position);

        DatabaseHelper.getUser(item.teacherId, new ServerCallback() {
            @Override
            public void onFinish(ServerResponse response) {
                if(response.success) {
                    User teacher = (User) response.data;
                    holder.name.setText(teacher.name);
                    Picasso.with(context).load(teacher.profilePictureUrl).into(holder.image);
                }
            }
        });

        holder.context = context;
        holder.postId = item.id;
        holder.courseId = courseId;
        holder.isDoctor = isDoctor;
        holder.text.setText(item.text);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(item.timestamp));
        holder.timestamp.setText(sdf.format(calendar.getTime()));
        return view;
    }

    static class ViewHolder {
        public String postId;
        public String courseId;
        public Context context;
        public boolean isDoctor;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.timestamp)
        TextView timestamp;

        @BindView(R.id.text)
        TextView text;

        @BindView(R.id.image)
        ImageView image;

        @BindView(R.id.edit)
        ImageView edit;

        @BindView(R.id.delete)
        ImageView delete;

        @OnClick(R.id.post)
        public void toggleActionsVisibility() {
            if(!isDoctor) return;
            if(edit.getVisibility() == View.VISIBLE) {
                edit.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
            } else {
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }
        }

        @OnClick(R.id.edit)
        public void editPost() {
            if(!isDoctor) return;
            toggleActionsVisibility();
            Intent editPost = new Intent(context, AddPost.class);
            editPost.putExtra(Utils.Constants.COURSE_ID, courseId);
            editPost.putExtra(Utils.Constants.POST_ID, postId);
            editPost.putExtra(Utils.Constants.POST_TEXT, text.getText().toString());
            context.startActivity(editPost);
        }

        @OnClick(R.id.delete)
        public void removePost() {
            if(!isDoctor) return;

            new AlertDialog.Builder(context)
                    .setTitle("Delete Post")
                    .setMessage("Are you sure you want to Delete Post?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toggleActionsVisibility();
                            DatabaseHelper.deletePost(courseId, postId, new ServerCallback() {
                                @Override
                                public void onFinish(ServerResponse response) {

                                }
                            });
                        }
                    }).setNegativeButton("No", null).show();
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}