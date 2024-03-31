package com.example.aplikacja.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.aplikacja.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void onContinueClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}