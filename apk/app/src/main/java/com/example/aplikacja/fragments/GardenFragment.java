package com.example.aplikacja.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aplikacja.R;
import com.example.aplikacja.activities.FlowerActivity;
import com.example.aplikacja.activities.MyGardenSelectedFlower;
import com.example.aplikacja.adapter.MyGardenAdapter;
import com.example.aplikacja.helpers.SelectListener;
import com.example.aplikacja.models.Flower;
import com.example.aplikacja.models.FlowerSharedPreferences;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;


public class GardenFragment extends Fragment {



    ShapeableImageView removeIcon;
    ShapeableImageView addFlowerBtn;

    RecyclerView myGardenRecyclerView;
    MyGardenAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // we have to check if user is logged in
        View view = inflater.inflate(R.layout.fragment_garden, container, false);

        removeIcon = view.findViewById(R.id.remove_garden);
        myGardenRecyclerView = view.findViewById(R.id.mygarden_recyclerview);
        addFlowerBtn = view.findViewById(R.id.mygarden_addflower_btn);


        FlowerSharedPreferences prefs = new FlowerSharedPreferences(view.getContext());
        List<Flower> gottenFlowers = prefs.getFlowers();

        adapter = new MyGardenAdapter(view.getContext(), gottenFlowers);

        myGardenRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        myGardenRecyclerView.setAdapter(adapter);


        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                FlowerSharedPreferences prefs = new FlowerSharedPreferences(view.getContext());
                prefs.removeNotesForFlower(pos);
                prefs.removeFlowerFromList(pos);
                gottenFlowers.remove(pos);
                adapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(myGardenRecyclerView);


        adapter.setSelectedListener(new SelectListener() {
            @Override
            public void onItemClicked(Flower flower) {
                Intent intent = new Intent(getContext(), MyGardenSelectedFlower.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("flower", flower);
                startActivity(intent);
            }
        });

        addFlowerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FlowerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlowerSharedPreferences prefs = new FlowerSharedPreferences(view.getContext());
                prefs.clearFlowerSharedPreferences();
                Toast.makeText(getContext(), "Usunieto wszystkie kwiaty", Toast.LENGTH_SHORT).show();
                gottenFlowers.clear();
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

}