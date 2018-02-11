package com.mina.collegehelper.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mina.collegehelper.CourseDetails;
import com.mina.collegehelper.R;
import com.mina.collegehelper.Utils;
import com.mina.collegehelper.activities.YearCourses;
import com.mina.collegehelper.model.datastructure.Year;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mina on 09/11/17.
 */

public class YearsListAdapter extends BaseAdapter {

    private ArrayList<Year> list;
    private Context context;


    public YearsListAdapter(Context context, ArrayList<Year> list) {
        this.list = list;
        this.context = context;
    }

    public YearsListAdapter(Context context) {
        this.list = new ArrayList();
        this.context = context;
    }

    public void setList(ArrayList<Year> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Year getItem(int position) {
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
            view = LayoutInflater.from(context).inflate(R.layout.year_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        final Year item = getItem(position);

        holder.name.setText(item.name);
//        holder.description.setText(item.description);
//
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, YearCourses.class);
                i.putExtra(Utils.Constants.YEAR, item);
                context.startActivity(i);
            }
        });
//
//        Picasso.with(context).load(item.imageUrl).into(holder.image);

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;

//        @BindView(R.id.description)
//        TextView description;
//
//        @BindView(R.id.image)
//        ImageView image;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}