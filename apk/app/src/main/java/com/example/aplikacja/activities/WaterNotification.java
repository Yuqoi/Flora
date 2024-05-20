package com.example.aplikacja.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aplikacja.R;
import com.example.aplikacja.models.Flower;
import com.google.android.material.imageview.ShapeableImageView;

public class WaterNotification extends AppCompatActivity {

    AppCompatImageButton returnButton;
    ShapeableImageView imageView;
    TextView flowerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_notification);

        returnButton = findViewById(R.id.notification_return_button);
        imageView = findViewById(R.id.notification_flower_image);
        flowerName = findViewById(R.id.notification_flower_name);

        Flower flower = (Flower) getIntent().getSerializableExtra("flowerclass");

        Glide.with(this)
                .load(flower.getImage())
                .placeholder(R.drawable.reload_icon)
                .error(R.drawable.error_icon)
                .into(imageView);
        flowerName.setText(flower.getName());

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}