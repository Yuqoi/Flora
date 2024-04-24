package com.example.aplikacja.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.aplikacja.R;
import com.example.aplikacja.adapters.FlowerAdapter;
import com.example.aplikacja.helpers.SelectListener;
import com.example.aplikacja.models.Flower;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FlowerActivity extends AppCompatActivity {

    RecyclerView flowersRecyclerView;
    FlowerAdapter flowerAdapter;
    ArrayList<Flower> list;
    SearchView searchView;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);

        flowersRecyclerView = findViewById(R.id.flowersRecyclerView);
        searchView = findViewById(R.id.searchView);
        list = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("flowers");
        flowersRecyclerView.setHasFixedSize(true);
        flowersRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        flowerAdapter = new FlowerAdapter(this, list);
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
                Toast.makeText(getApplicationContext(), "Kliknales na " + flower.getName(), Toast.LENGTH_SHORT).show();
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