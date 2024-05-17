package com.example.aplikacja.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.aplikacja.models.Flower;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FlowerSharedPreferences {
    private static final String PREF_NAME = "selected_flowers";
    private static final String KEY_FLOWERS = "flowers";

    public static final String KEY_NOTES = "notes";

    private SharedPreferences preferences;
    private Gson gson;

    boolean isDuplicated;

    public FlowerSharedPreferences(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    public void clearFlowerSharedPreferences(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
    }
    public void removeNotesForFlower(int pos) {
        Flower flower = getFlowers().get(pos);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(flower.getName() + "_notes");
        editor.apply();
    }

    public void addNotesToFlower(Flower flower, String text){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(flower.getName() + "_notes", text);
        editor.apply();
    }

    public String getNotesForFlower(Flower flower) {
        return preferences.getString(flower.getName() + "_notes", "");
    }

    public void removeFlowerFromList(int pos){
        String json = preferences.getString(KEY_FLOWERS, "");
        Type type = new TypeToken<List<Flower>>(){}.getType();
        List<Flower> flowerList = gson.fromJson(json, type);

        flowerList.remove(pos);
        saveSelectedFlowers(flowerList);
    }
    public void addSelectedFlower(Flower flower) {
        List<Flower> selectedFlowers = getFlowers();
        checkDuplicated(flower);
        if (!isDuplicated) {
            selectedFlowers.add(flower);
            saveSelectedFlowers(selectedFlowers);
        }
    }
    public boolean checkDuplicated(Flower flower){
        List<Flower> selectedFlowers = getFlowers();
        for (Flower existingFlower : selectedFlowers) {
            if (existingFlower.getName().equals(flower.getName())) {
                isDuplicated = true;
                return isDuplicated;
            }
        }
        isDuplicated = false;
        return isDuplicated;
    }

    public boolean getIsDuplicated(){
        return isDuplicated;
    }


    public List<Flower> getFlowers() {
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
