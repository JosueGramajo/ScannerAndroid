package com.example.josuegramajo.infinitywarpresalescanner.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.josuegramajo.infinitywarpresalescanner.R;
import com.example.josuegramajo.infinitywarpresalescanner.objects.LogObject;

import java.util.ArrayList;

/**
 * Created by josuegramajo on 3/23/18.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<LogObject> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView url,  state, date;

        public MyViewHolder(View view) {
            super(view);
            url = (TextView) view.findViewById(R.id.text_url);
            state = (TextView) view.findViewById(R.id.text_state);
            date = (TextView) view.findViewById(R.id.text_date);
        }
    }


    public RecyclerAdapter(ArrayList<LogObject> moviesList) {
        this.list = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LogObject movie = list.get(position);
        holder.url.setText(movie.getUrl());
        holder.state.setText(movie.getState());
        holder.date.setText(movie.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}