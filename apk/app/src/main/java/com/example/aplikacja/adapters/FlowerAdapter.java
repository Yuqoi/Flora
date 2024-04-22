package com.example.aplikacja.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.aplikacja.R;
import com.example.aplikacja.helpers.SelectListener;
import com.example.aplikacja.models.Flower;

import java.util.ArrayList;
import java.util.List;

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Flower> list;

    private SelectListener listener;

    public FlowerAdapter(Context context, ArrayList<Flower> list) {
        this.context = context;
        this.list = list;

    }

    public void setSelectedListener(SelectListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Flower flower = list.get(position);

        holder.flowerName.setText(flower.getName());
        Glide.with(context.getApplicationContext())
                .load(flower.getImage())
                .placeholder(R.drawable.user_icon)
                .error(R.drawable.home_icon)
                .into(holder.flowerImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClicked(flower);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Flower> filteredList){
        list = (ArrayList<Flower>) filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView flowerName;
        ImageView flowerImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            flowerName = itemView.findViewById(R.id.flowerName);
            flowerImage = itemView.findViewById(R.id.flowerImage);

        }
    }
}
