package com.example.aplikacja.adapter;

import android.content.Context;
import android.media.metrics.Event;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplikacja.R;
import com.example.aplikacja.helpers.SelectListener;
import com.example.aplikacja.models.Flower;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class HomeFlowerAdapter extends RecyclerView.Adapter<HomeFlowerAdapter.ViewHolder>{

    Context context;
    List<Flower> flowerList;

    private SelectListener listener;


    public HomeFlowerAdapter(Context context, List<Flower> flowerList) {
        this.context = context;
        this.flowerList = flowerList;
    }

    public void setSelectedListener(SelectListener listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_flower_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.flowerName.setText(flowerList.get(position).getName());

        Glide.with(context.getApplicationContext())
                .load(flowerList.get(position).getImage())
                .placeholder(R.drawable.reload_icon)
                .error(R.drawable.error_icon)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClicked(flowerList.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return flowerList.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView imageView;
        TextView flowerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.home_flower_image);
            flowerName = itemView.findViewById(R.id.home_flower_name);

        }
    }
}
