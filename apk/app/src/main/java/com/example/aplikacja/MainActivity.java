package com.example.aplikacja;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navHome){
                    changeFragment(new HomeFragment(), false);
                } else if (itemId == R.id.navCamera) {
                    changeFragment(new CameraFragment(), false);
                } else if (itemId == R.id.navProfile) {
                    changeFragment(new UserFragment(), false);
                }else{ // navGarden
                    changeFragment(new GardenFragment(), false);
                }

                return true;
            }
        });
        changeFragment(new HomeFragment(), true);
    }

    private void changeFragment(Fragment fragment, boolean isInitialized){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (isInitialized) {
            ft.add(R.id.frameLayout, fragment);
        }else{
            ft.replace(R.id.frameLayout, fragment);
        }
        ft.commit();
    }
}