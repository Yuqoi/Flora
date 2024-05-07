package com.example.aplikacja.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikacja.R;
import com.example.aplikacja.helpers.FlowerAdapter;
import com.example.aplikacja.helpers.SelectListener;
import com.example.aplikacja.models.Flower;
import com.example.aplikacja.models.FlowerSharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FlowerActivity extends AppCompatActivity {

    RecyclerView flowersRecyclerView;
    FlowerAdapter flowerAdapter;
    ArrayList<Flower> list;
    SearchView searchView;

    AppCompatImageButton returnButton;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);

        flowersRecyclerView = findViewById(R.id.flowersRecyclerView);
        searchView = findViewById(R.id.searchView);
        list = new ArrayList<>();


        databaseReference = FirebaseDatabase.getInstance().getReference("kwiaty");
        flowersRecyclerView.setHasFixedSize(true);
        flowersRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        flowerAdapter = new FlowerAdapter(this, list);
        flowersRecyclerView.setAdapter(flowerAdapter);

        returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlowerActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Flower flower = new Flower((String) dataSnapshot.child("name").getValue(),
                            (String) dataSnapshot.child("flowerImage").getValue(),
                            (String) dataSnapshot.child("description").getValue(),
                            (String) dataSnapshot.child("bloomTime").getValue(),
                            (List<String>) dataSnapshot.child("colors").getValue(),
                            (String) dataSnapshot.child("family").getValue(),
                            (String) dataSnapshot.child("nativeRegion").getValue(),
                            (String) dataSnapshot.child("scientificName").getValue(),
                            (String) dataSnapshot.child("soilType").getValue(),
                            (String) dataSnapshot.child("sunRequirements").getValue(),
                            (String) dataSnapshot.child("wateringNeeds").getValue(),
                            (String) dataSnapshot.child("avgHeight").getValue(),
                            (String) dataSnapshot.child("avgSpread").getValue(),
                            (String) dataSnapshot.child("toxicity").getValue(),
                            (String) dataSnapshot.child("susceptibilityToPests").getValue(),
                            (String) dataSnapshot.child("usability").getValue(),
                            (String) dataSnapshot.child("difficulty").getValue());
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
                Intent intent = new Intent(FlowerActivity.this, SelectedFlower.class);
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