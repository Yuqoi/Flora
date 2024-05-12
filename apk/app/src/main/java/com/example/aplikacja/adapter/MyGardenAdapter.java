package com.example.aplikacja.adapter;

import android.content.Context;
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

public class MyGardenAdapter extends RecyclerView.Adapter<MyGardenAdapter.MyGardenViewHolder> {

    Context context;
    List<Flower> flowerList;

    private SelectListener listener;

    public MyGardenAdapter(Context context, List<Flower> flowerList) {
        this.context = context;
        this.flowerList = flowerList;
    }

    public void setSelectedListener(SelectListener listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyGardenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyGardenViewHolder(LayoutInflater.from(context).inflate(R.layout.mygarden_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyGardenViewHolder holder, int position) {
        Glide.with(this.context)
                .load(flowerList.get(position).getImage())
                .placeholder(R.drawable.reload_icon)
                .error(R.drawable.error_icon)
                .into(holder.flowerImage);
        holder.flowerScientificName.setText(flowerList.get(position).getScientificName());
        holder.flowerName.setText(flowerList.get(position).getName());

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

    public class MyGardenViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView flowerImage;
        TextView flowerName, flowerScientificName;

        public MyGardenViewHolder(@NonNull View itemView) {
            super(itemView);

            flowerImage = itemView.findViewById(R.id.mygarden_image);
            flowerName = itemView.findViewById(R.id.mygarden_flower_name);
            flowerScientificName = itemView.findViewById(R.id.mygarden_flower_scientific);
        }
    }
}
