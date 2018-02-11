package com.mina.collegehelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mina.collegehelper.R;
import com.mina.collegehelper.model.datastructure.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mina on 25/11/17.
 */

public class UsersListAdapter extends BaseAdapter {

    private ArrayList<User> list;
    private Context context;


    public UsersListAdapter(Context context, ArrayList<User> list) {
        this.list = list;
        this.context = context;
    }

    public UsersListAdapter(Context context) {
        this.list = new ArrayList();
        this.context = context;
    }

    public void setList(ArrayList<User> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public User getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        ButterKnife.setDebug(true);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        User item = getItem(position);

        holder.name.setText(item.name);
        Picasso.with(context).load(item.profilePictureUrl).into(holder.image);

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.image)
        ImageView image;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}