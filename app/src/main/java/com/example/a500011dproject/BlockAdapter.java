package com.example.a500011dproject;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BlockAdapter extends RecyclerView.Adapter<BlockAdapter.ViewHolder> {

    ArrayList<String> places;
    ArrayList<Date> dates;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView placeTextView;
        public TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            placeTextView = (TextView) itemView.findViewById(R.id.place);
            dateTextView = (TextView) itemView.findViewById(R.id.date);
        }
    }

    public BlockAdapter(ArrayList<Date> dates, ArrayList<String> places) {
        this.places = places;
        this.dates = dates;
    }

    public BlockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View blockView = inflater.inflate(R.layout.item_block, parent, false);

        ViewHolder viewHolder = new ViewHolder(blockView);
        return viewHolder;
    }

    public void onBindViewHolder(BlockAdapter.ViewHolder holder, int position) {
        String place = places.get(position);
        Date date = dates.get(position);

        TextView placeTextView = holder.placeTextView;
        placeTextView.setText(place);

        TextView dateTextView = holder.dateTextView;
        dateTextView.setText(date.toString());
    }

    public int getItemCount() {
        return dates.size();
    }
}
