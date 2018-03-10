package com.mina.collegehelper.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mina.collegehelper.activities.CourseDetails;
import com.mina.collegehelper.R;
import com.mina.collegehelper.Utils;
import com.mina.collegehelper.activities.CoursePosts;
import com.mina.collegehelper.model.datastructure.Course;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mina on 09/11/17.
 */

public class CoursesListAdapter extends BaseAdapter {

    private ArrayList<Course> list;
    private Context context;
    private boolean openCourseDetails;


    public CoursesListAdapter(Context context, ArrayList<Course> list) {
        this.list = list;
        this.context = context;
    }

    public CoursesListAdapter(Context context, boolean openCourseDetails) {
        this.list = new ArrayList();
        this.context = context;
        this.openCourseDetails = openCourseDetails;
    }

    public void setList(ArrayList<Course> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Course getItem(int position) {
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

        final Course item = getItem(position);

        holder.name.setText(item.name);
        holder.description.setText(item.description);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class destination = openCourseDetails ? CourseDetails.class : CoursePosts.class;
                Intent i = new Intent(context, destination);
                i.putExtra(Utils.Constants.COURSE_ID, item.id);
                i.putExtra(Utils.Constants.COURSE_NAME, item.name);
                context.startActivity(i);
            }
        });

        Picasso.with(context).load(item.imageUrl).into(holder.image);

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.description)
        TextView description;

        @BindView(R.id.image)
        ImageView image;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}