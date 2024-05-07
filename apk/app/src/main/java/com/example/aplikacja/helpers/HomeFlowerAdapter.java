package com.example.aplikacja.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplikacja.R;
import com.example.aplikacja.models.Flower;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class HomeFlowerAdapter extends RecyclerView.Adapter<HomeFlowerAdapter.ViewHolder>{

    Context context;
    List<Flower> flowerList;

    public HomeFlowerAdapter(Context context, List<Flower> flowerList) {
        this.context = context;
        this.flowerList = flowerList;
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
