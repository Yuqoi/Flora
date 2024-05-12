package com.example.aplikacja.fragments;

import android.content.Intent;
import android.os.Bundle;


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

import com.example.aplikacja.activities.FlowerActivity;
import com.example.aplikacja.R;

import com.example.aplikacja.activities.NaukaActivity;
import com.example.aplikacja.helpers.FragmentHelper;
import com.example.aplikacja.adapter.HomeFlowerAdapter;
import com.example.aplikacja.models.Flower;
import com.example.aplikacja.models.FlowerSharedPreferences;

import java.util.List;


public class HomeFragment extends Fragment implements FragmentHelper {

    CardView mojprofil;
    CardView kamerai;
    CardView twojogrod;
    CardView nauka;

    SearchView searchView;
    RecyclerView flowersRecyclerView;

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mojprofil = view.findViewById(R.id.moj_profil_view);
        kamerai = view.findViewById(R.id.kamera_view);
        twojogrod = view.findViewById(R.id.twoj_ogrod_view);
        nauka = view.findViewById(R.id.nauka_view);
        searchView = view.findViewById(R.id.search_bar);
        flowersRecyclerView = view.findViewById(R.id.home_flowers_recyclerview);
        textView = view.findViewById(R.id.home_check_text);
        FlowerSharedPreferences prefs = new FlowerSharedPreferences(view.getContext());
        List<Flower> flowerList = prefs.getFlowers();

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        flowersRecyclerView.setLayoutManager(layoutManager);
        flowersRecyclerView.setAdapter(new HomeFlowerAdapter(view.getContext(), flowerList));


        setSearchView();

        clickListener();

        return view;
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
                changeFragment(new CameraFragment());
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