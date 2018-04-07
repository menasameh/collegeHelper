package com.mina.collegehelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mina.collegehelper.R;
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

/**
 * Created by mina on 09/11/17.
 */

public class PostsListAdapter extends BaseAdapter {

    private ArrayList<Post> list;
    private Context context;


    public PostsListAdapter(Context context, ArrayList<Post> list) {
        this.list = list;
        this.context = context;
    }

    public PostsListAdapter(Context context) {
        this.list = new ArrayList();
        this.context = context;
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

        holder.text.setText(item.text);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(item.timestamp));
        holder.timestamp.setText(sdf.format(calendar.getTime()));
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.timestamp)
        TextView timestamp;

        @BindView(R.id.text)
        TextView text;

        @BindView(R.id.image)
        ImageView image;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}