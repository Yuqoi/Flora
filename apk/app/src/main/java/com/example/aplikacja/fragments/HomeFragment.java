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
import android.widget.SearchView;

import com.example.aplikacja.activities.FlowerActivity;
import com.example.aplikacja.R;

import com.example.aplikacja.helpers.FragmentHelper;



public class HomeFragment extends Fragment implements FragmentHelper {

    CardView mojprofil;
    CardView kamerai;
    CardView twojogrod;
    CardView poradniki;
    CardView nauka;

    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mojprofil = view.findViewById(R.id.moj_profil_view);
        kamerai = view.findViewById(R.id.kamera_view);
        twojogrod = view.findViewById(R.id.twoj_ogrod_view);
        poradniki = view.findViewById(R.id.poradnik_view);
        nauka = view.findViewById(R.id.nauka_view);


        searchView = view.findViewById(R.id.search_bar);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FlowerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        clickListener();


        return view;
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
        poradniki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new CameraFragment());
            }
        });
        nauka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new CameraFragment());
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
        ft.commit();
    }
}