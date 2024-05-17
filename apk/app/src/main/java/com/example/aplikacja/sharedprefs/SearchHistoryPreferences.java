package com.example.aplikacja.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.aplikacja.models.Flower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchHistoryPreferences {
    private static final String PREFS_NAME = "searchHistory";
    private static final String SEARCH_HISTORY_KEY = "SearchHistory";

    private SharedPreferences sharedPreferences;



    public SearchHistoryPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public void removeTextFromHistory(String text) {
        List<String> savedTexts = loadSearchHistory();
        savedTexts.remove(text);
        saveSearchHistory(savedTexts);
    }




    public void saveSearchHistory(List<String> searchHistory) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder sb = new StringBuilder();
        for (String query : searchHistory) {
            sb.append(query).append(";");
        }
        editor.putString(SEARCH_HISTORY_KEY, sb.toString());
        editor.apply();
    }

    public List<String> loadSearchHistory() {
        String savedHistory = sharedPreferences.getString(SEARCH_HISTORY_KEY, "");
        if (savedHistory.isEmpty()) {
            return new ArrayList<>();
        } else {
            String[] items = savedHistory.split(";");
            return new ArrayList<>(Arrays.asList(items));
        }
    }

    public void addSearchQuery(String query) {
        List<String> searchHistory = loadSearchHistory();
        searchHistory.add(query);
        saveSearchHistory(searchHistory);
    }

    public void clearSearchHistory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SEARCH_HISTORY_KEY);
        editor.apply();
    }
}
