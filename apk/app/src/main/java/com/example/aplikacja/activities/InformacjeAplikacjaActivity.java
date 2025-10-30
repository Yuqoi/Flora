package com.example.aplikacja.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.os.Bundle;
import android.view.View;

import com.example.aplikacja.R;

public class InformacjeAplikacjaActivity extends AppCompatActivity {

    AppCompatImageButton returnButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacjeaplikacja);

        returnButton = findViewById(R.id.informacje_return_button);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}