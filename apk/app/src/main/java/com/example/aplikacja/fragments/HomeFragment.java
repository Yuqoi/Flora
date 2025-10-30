package com.example.aplikacja.fragments;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplikacja.activities.CameraActivity;
import com.example.aplikacja.activities.FlowerActivity;
import com.example.aplikacja.R;

import com.example.aplikacja.activities.MyGardenSelectedFlower;
import com.example.aplikacja.activities.NaukaActivity;
import com.example.aplikacja.adapter.TrendsAdapter;
import com.example.aplikacja.helpers.FragmentHelper;
import com.example.aplikacja.adapter.HomeFlowerAdapter;
import com.example.aplikacja.helpers.SelectListener;
import com.example.aplikacja.models.Flower;
import com.example.aplikacja.models.Trends;
import com.example.aplikacja.sharedprefs.FlowerSharedPreferences;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements FragmentHelper {

    CardView mojprofil;
    CardView kamerai;
    CardView twojogrod;
    CardView nauka;

    SearchView searchView;
    RecyclerView flowersRecyclerView;
    RecyclerView trendingRecyclerView;
    TrendsAdapter trendsAdapter;

    View emptyView;

    TextView userName;

    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;
    DatabaseReference databaseReference;


    ArrayList<Trends> trendsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mojprofil = view.findViewById(R.id.moj_profil_view);
        kamerai = view.findViewById(R.id.kamera_view);
        twojogrod = view.findViewById(R.id.twoj_ogrod_view);
        nauka = view.findViewById(R.id.nauka_view);
        searchView = view.findViewById(R.id.search_bar);
        flowersRecyclerView = view.findViewById(R.id.home_flowers_recyclerview);
        userName = view.findViewById(R.id.home_user_name);
        emptyView = view.findViewById(R.id.emptyView);
        trendingRecyclerView = view.findViewById(R.id.home_trending_recyclerview);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        trendsList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("trendy");


        LinearLayoutManager layoutForTrends = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        trendsAdapter = new TrendsAdapter(view.getContext(), trendsList);
        trendingRecyclerView.setAdapter(trendsAdapter);
        trendingRecyclerView.setLayoutManager(layoutForTrends);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Trends trend = new Trends((String) dataSnapshot.child("name").getValue(),
                            (String) dataSnapshot.child("image_url").getValue());
                    trendsList.add(trend);
                }
                trendsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FlowerSharedPreferences prefs = new FlowerSharedPreferences(view.getContext());
        List<Flower> flowerList = prefs.getFlowers();

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        HomeFlowerAdapter adapter = new HomeFlowerAdapter(view.getContext(), flowerList);
        flowersRecyclerView.setLayoutManager(layoutManager);

        flowersRecyclerView.setAdapter(adapter);
        checkEmptyState(adapter);

        adapter.setSelectedListener(new SelectListener() {
            @Override
            public void onItemClicked(Flower flower) {
                Intent intent = new Intent(getContext(), MyGardenSelectedFlower.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("flower", flower);
                startActivity(intent);
            }
        });


        if (user != null){
            DocumentReference documentReference = db.collection("users").document(user.getUid());
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    userName.setText(documentSnapshot.getString("username"));
                }
            });
        }else{
            userName.setText("Gość");
        }



        setSearchView();
        clickListener();

        return view;
    }

    private void checkEmptyState(HomeFlowerAdapter adapter) {
        if (adapter.getItemCount() == 0) {
            flowersRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            flowersRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void setSearchView(){
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchIntent();
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchIntent();
            }
        });
    }
    private void showSearchIntent(){
        Intent intent = new Intent(getContext(), FlowerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void clickListener(){
        mojprofil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new UserFragment());
            }
        });
        kamerai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIntent(new CameraActivity());
            }
        });
        twojogrod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new GardenFragment());
            }
        });
        nauka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIntent(new NaukaActivity());
            }
        });
    }

    @Override
    public void changeFragment(Fragment fragment, boolean isInitialized) {

    }

    @Override
    public void changeFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void changeIntent(Object i){
        Intent intent = new Intent(getContext(), i.getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}