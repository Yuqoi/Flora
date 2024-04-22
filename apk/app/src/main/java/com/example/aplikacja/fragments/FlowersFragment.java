package com.example.aplikacja.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aplikacja.adapters.FlowerAdapter;
import com.example.aplikacja.helpers.SelectListener;
import com.example.aplikacja.R;
import com.example.aplikacja.models.Flower;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FlowersFragment extends Fragment {

    RecyclerView flowersRecyclerView;
    FlowerAdapter flowerAdapter;
    ArrayList<Flower> list;
    SearchView searchView;



    DatabaseReference databaseReference;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_flowers, container, false);

        flowersRecyclerView = view.findViewById(R.id.flowersRecyclerView);
        searchView = view.findViewById(R.id.searchView);
        list = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("flowers");
        flowersRecyclerView.setHasFixedSize(true);
        flowersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        flowerAdapter = new FlowerAdapter(getContext(), list);
        flowersRecyclerView.setAdapter(flowerAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Flower flower = new Flower((String) dataSnapshot.child("name").getValue(), (String) dataSnapshot.child("flowerImage").getValue());

                    list.add(flower);

                }
                flowerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        flowerAdapter.setSelectedListener(new SelectListener() {
            @Override
            public void onItemClicked(Flower flower) {
                Toast.makeText(getContext(), "Kliknales na " + flower.getName(), Toast.LENGTH_SHORT).show();
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
                return true;
            }
        });

        return view;
    }

    private void filter(String newText) {
        List<Flower> filteredList = new ArrayList<>();
        for (Flower item : list){
            if (item.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        flowerAdapter.filterList(filteredList);
    }
}