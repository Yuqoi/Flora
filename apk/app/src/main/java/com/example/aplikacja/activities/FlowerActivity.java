package com.example.aplikacja.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.aplikacja.adapter.SearchHistoryAdapter;
import com.example.aplikacja.helpers.StringSelectListener;
import com.example.aplikacja.models.SearchItem;
import com.example.aplikacja.sharedprefs.SearchHistoryPreferences;
import com.google.android.material.search.SearchBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikacja.R;
import com.example.aplikacja.adapter.FlowerAdapter;
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
    RecyclerView searchHistoryRecyclerView;
    FlowerAdapter flowerAdapter;
    SearchView searchView;

    AppCompatImageButton returnButton;

    DatabaseReference databaseReference;

    List<SearchItem> searchList;
    ArrayList<Flower> list;

    SearchHistoryAdapter searchAdapter;
    SearchHistoryPreferences searchHistoryPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);

        searchHistoryRecyclerView = findViewById(R.id.flower_search_history);
        flowersRecyclerView = findViewById(R.id.flowersRecyclerView);
        searchView = findViewById(R.id.searchView);
        list = new ArrayList<>();
        returnButton = findViewById(R.id.return_button);
        searchList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("kwiaty");
        flowersRecyclerView.setHasFixedSize(true);
        flowersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        flowerAdapter = new FlowerAdapter(this, list);
        flowersRecyclerView.setAdapter(flowerAdapter);

        searchHistoryPreferences = new SearchHistoryPreferences(this);
        List<String> savedHistory = searchHistoryPreferences.loadSearchHistory();
        for (String q : savedHistory){
            searchList.add(new SearchItem(q));
        }




        searchAdapter = new SearchHistoryAdapter(this, searchList);
        searchHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        searchHistoryRecyclerView.setAdapter(searchAdapter);

        searchAdapter.setSelectedListener(new StringSelectListener() {
            @Override
            public void setSelectedItem(SearchItem text) {
                savedHistory.remove(text.getText());
                searchHistoryPreferences.saveSearchHistory(savedHistory);
                searchView.setQuery(text.getText(), false);
            }
        });

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
                            (String) dataSnapshot.child("difficulty").getValue(),
                            dataSnapshot.child("whenToWater").getValue(Integer.class));
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
                if (!query.isEmpty()) {
                    searchList.add(new SearchItem(query));
                    searchAdapter.notifyItemInserted(searchList.size() - 1);
                    searchHistoryPreferences.addSearchQuery(query);
                    searchView.setQuery("", false);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });



    }
    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
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