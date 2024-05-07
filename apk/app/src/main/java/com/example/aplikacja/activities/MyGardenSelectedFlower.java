package com.example.aplikacja.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.aplikacja.fragments.GardenFragment;
import com.example.aplikacja.fragments.InformacjeFragment;
import com.example.aplikacja.helpers.FlowerViewModel;
import com.example.aplikacja.helpers.FragmentPageAdapter;
import com.example.aplikacja.models.Flower;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.example.aplikacja.R;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyGardenSelectedFlower extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ShapeableImageView flowerImage;

    AppCompatImageButton returnButton;

    FragmentPageAdapter adapter;

    private FlowerViewModel flowerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garden_selected_flower);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        flowerImage = findViewById(R.id.mygarden_image);

        returnButton = findViewById(R.id.mygarden_selected_return);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentPageAdapter(fm, getLifecycle());
        viewPager.setAdapter(adapter);

        Flower gottenFlower = (Flower) getIntent().getSerializableExtra("flower");

        flowerViewModel = new ViewModelProvider(this).get(FlowerViewModel.class);
        flowerViewModel.setFlower(gottenFlower);


        Glide.with(getApplicationContext())
                .load(gottenFlower.getImage())
                .placeholder(R.drawable.reload_icon)
                .error(R.drawable.error_icon)
                .into(flowerImage);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });
    }
}