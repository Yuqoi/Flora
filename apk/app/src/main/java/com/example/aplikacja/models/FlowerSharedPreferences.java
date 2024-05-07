package com.example.aplikacja.models;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FlowerSharedPreferences {
    private static final String PREF_NAME = "selected_flowers";
    private static final String KEY_FLOWERS = "flowers";

    private SharedPreferences preferences;
    private Gson gson;

    public FlowerSharedPreferences(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    public void clearFlowerSharedPreferences(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
    }

    public void removeFlowerFromList(int pos){
        String json = preferences.getString(KEY_FLOWERS, "");
        Type type = new TypeToken<List<Flower>>(){}.getType();
        List<Flower> flowerList = gson.fromJson(json, type);

        flowerList.remove(pos);
        saveSelectedFlowers(flowerList);
    }
    public void addSelectedFlower(Flower flower) {
        List<Flower> selectedFlowers = getSelectedFlowers();
        selectedFlowers.add(flower);
        saveSelectedFlowers(selectedFlowers);
    }

    public List<Flower> getSelectedFlowers() {
        String json = preferences.getString(KEY_FLOWERS, "");
        Type type = new TypeToken<List<Flower>>(){}.getType();
        List<Flower> selectedFlowers = gson.fromJson(json, type);
        if (selectedFlowers == null) {
            selectedFlowers = new ArrayList<>();
        }
        return selectedFlowers;
    }

    private void saveSelectedFlowers(List<Flower> selectedFlowers) {
        String json = gson.toJson(selectedFlowers);
        preferences.edit().putString(KEY_FLOWERS, json).apply();
    }

}
