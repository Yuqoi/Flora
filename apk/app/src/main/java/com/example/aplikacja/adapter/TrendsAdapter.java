package com.example.aplikacja.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplikacja.R;
import com.example.aplikacja.models.Flower;
import com.example.aplikacja.models.Trends;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class TrendsAdapter extends RecyclerView.Adapter<TrendsAdapter.MyViewHolder> {

    Context context;
    ArrayList<Trends> trendsList;

    public TrendsAdapter(Context context, ArrayList<Trends> trendsList) {
        this.context = context;
        this.trendsList = trendsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.trends_item, parent, false);



        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Trends trends = trendsList.get(position);
        Glide.with(context.getApplicationContext())
                .load(trends.getImage())
                .placeholder(R.drawable.reload_icon)
                .error(R.drawable.error_icon)
                .into(holder.flowerImage);
        holder.flowerName.setText(trends.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q="+trends.getName())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return trendsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView flowerName;
        ShapeableImageView flowerImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            flowerName = itemView.findViewById(R.id.trends_name);
            flowerImage = itemView.findViewById(R.id.trends_image);
        }
    }
}
