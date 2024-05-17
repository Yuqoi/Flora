package com.example.aplikacja.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import android.widget.SearchView;
import android.widget.Toast;

import com.example.aplikacja.R;
import com.example.aplikacja.activities.FlowerActivity;
import com.example.aplikacja.activities.MyGardenSelectedFlower;
import com.example.aplikacja.adapter.MyGardenAdapter;
import com.example.aplikacja.helpers.SelectListener;
import com.example.aplikacja.models.Flower;
import com.example.aplikacja.sharedprefs.FlowerSharedPreferences;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;


public class GardenFragment extends Fragment {


    SearchView searchView;

    ShapeableImageView removeIcon;
    ShapeableImageView addFlowerBtn;

    RecyclerView myGardenRecyclerView;
    MyGardenAdapter adapter;

    List<Flower> gottenFlowers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_garden, container, false);

        removeIcon = view.findViewById(R.id.remove_garden);
        myGardenRecyclerView = view.findViewById(R.id.mygarden_recyclerview);
        addFlowerBtn = view.findViewById(R.id.mygarden_addflower_btn);
        searchView = view.findViewById(R.id.garden_find_flower);

        FlowerSharedPreferences prefs = new FlowerSharedPreferences(view.getContext());
        gottenFlowers = prefs.getFlowers();

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Czy na pewno chcesz usunąć wszystkie kwiaty z kolekcji?");
                builder.setTitle("Usuń kwiaty");
                builder.setCancelable(false);


                builder.setNegativeButton("Nie", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                builder.setPositiveButton("Tak", (DialogInterface.OnClickListener) (dialog, which) -> {
                    FlowerSharedPreferences prefs = new FlowerSharedPreferences(view.getContext());
                    prefs.clearFlowerSharedPreferences();
                    Toast.makeText(getContext(), "Usunieto wszystkie kwiaty", Toast.LENGTH_SHORT).show();
                    gottenFlowers.clear();
                    adapter.notifyDataSetChanged();

                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        return view;
    }

    private void filter(String newText) {
        List<Flower> filteredList = new ArrayList<>();
        for (Flower item : gottenFlowers){
            if (item.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

}