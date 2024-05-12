package com.example.aplikacja.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.aplikacja.R;
import com.example.aplikacja.helpers.FlowerViewModel;
import com.example.aplikacja.models.Flower;
import com.example.aplikacja.models.FlowerSharedPreferences;


public class NotatkiFragment extends Fragment {

    EditText notatkiText;

    private FlowerViewModel flowerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notatki, container, false);

        notatkiText = view.findViewById(R.id.notatki_text);

        flowerViewModel = new ViewModelProvider(requireActivity()).get(FlowerViewModel.class);
        Flower flower = flowerViewModel.getFlower();

        FlowerSharedPreferences prefs = new FlowerSharedPreferences(view.getContext());
        notatkiText.setText(prefs.getNotesForFlower(flower));

        notatkiText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                prefs.addNotesToFlower(flower, s.toString());
            }
        });

        return view;
    }
}