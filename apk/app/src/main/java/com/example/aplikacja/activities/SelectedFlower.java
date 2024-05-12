package com.example.aplikacja.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.aplikacja.R;
import com.example.aplikacja.models.Flower;
import com.example.aplikacja.models.FlowerSharedPreferences;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Iterator;
import java.util.List;

public class SelectedFlower extends AppCompatActivity {

    AppCompatImageButton leave_button;
    AppCompatButton addSelectedFlowerBtn;

    ShapeableImageView flowerImage;
    TextView flowerName;
    TextView flowerScietificName;
    TextView flowerDesc;
    TextView flowerColors;
    TextView flowerFamily;
    TextView flowerBloomTime;
    TextView flowerAvgHeight;
    TextView flowerAvgSpread;
    TextView flowerRegion;

    TextView flowerSoil;
    TextView flowerSun;
    TextView flowerWater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_flower);

        Flower flower = (Flower) getIntent().getSerializableExtra("flower");

        leave_button = findViewById(R.id.return_button);
        addSelectedFlowerBtn = findViewById(R.id.add_selected_flower);

        flowerImage = findViewById(R.id.selected_flower_image);
        flowerName = findViewById(R.id.selected_flower_name);
        flowerDesc = findViewById(R.id.selected_flower_desc);
        flowerScietificName = findViewById(R.id.selected_flower_scientificname);
        flowerColors = findViewById(R.id.selected_flower_colors);
        flowerFamily = findViewById(R.id.selected_flower_family);
        flowerBloomTime = findViewById(R.id.selected_flower_bloomtime);
        flowerAvgHeight = findViewById(R.id.selected_flower_height);
        flowerAvgSpread = findViewById(R.id.selected_flower_spread);
        flowerRegion = findViewById(R.id.selected_flower_region);

        flowerSoil = findViewById(R.id.selected_flower_soil);
        flowerSun = findViewById(R.id.selected_flower_sun);
        flowerWater = findViewById(R.id.selected_flower_watering);

        StringBuilder colors = new StringBuilder();
        for (Iterator<String> it = flower.getColors().iterator(); it.hasNext(); ) {
            String s = it.next();
            colors.append(s).append(" ");
        }
        flowerName.setText(flower.getName());
        flowerScietificName.setText(flower.getScientificName());
        flowerDesc.setText(flower.getDesc());

        flowerFamily.setText(flower.getFamily());
        flowerColors.setText(colors);
        flowerBloomTime.setText(flower.getBloomTime());
        flowerAvgSpread.setText(flower.getAvgSpread());
        flowerAvgHeight.setText(flower.getAvgHeight());
        flowerRegion.setText(flower.getNativeRegion());

        flowerSoil.setText(flower.getSoilType());
        flowerSun.setText(flower.getSunRequirements());
        flowerWater.setText(flower.getWateringNeeds());


        Glide.with(getApplicationContext())
                .load(flower.getImage())
                .placeholder(R.drawable.reload_icon)
                .error(R.drawable.error_icon)
                .into(flowerImage);



        addSelectedFlowerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlowerSharedPreferences preferences = new FlowerSharedPreferences(SelectedFlower.this);
                preferences.addSelectedFlower(flower);
                System.out.println(preferences.getIsDuplicated());
                if (preferences.getIsDuplicated()){
                    Toast.makeText(SelectedFlower.this, "Kwiat jest w ogrodzie", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SelectedFlower.this, "Kwiat zostal dodany", Toast.LENGTH_SHORT).show();
                }

            }
        });

        leave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedFlower.this, FlowerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });
    }
}