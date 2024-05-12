package com.example.aplikacja.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aplikacja.R;
import com.example.aplikacja.fragments.CameraFragment;
import com.example.aplikacja.fragments.GardenFragment;
import com.example.aplikacja.fragments.HomeFragment;
import com.example.aplikacja.fragments.LoginFragment;
import com.example.aplikacja.fragments.UserFragment;
import com.example.aplikacja.helpers.FragmentHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements FragmentHelper {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameContainer;

    private CardView profileView;
    private CardView kameraView;
    private CardView ogrodView;
    private CardView poradnikView;
    private CardView naukaView;

    TextView welocomeUserText;

    FirebaseAuth auth;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameContainer = findViewById(R.id.fragment_container);

//        Check if user is logged
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.navHome) {
                    changeFragment(new HomeFragment(), false);
                } else if (itemId == R.id.navGarden) {
                    changeFragment(new GardenFragment(), false);
                } else if (itemId == R.id.navCamera) {
                    changeFragment(new CameraFragment(), false);
                } else{ // profile fragment
                    changeFragment(new UserFragment(), false);
                }

                return true;
            }
        });
        changeFragment(new HomeFragment(), true);
    }




    @Override
    public void changeFragment(Fragment fragment, boolean isInitialized) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (isInitialized) {
            ft.add(R.id.fragment_container, fragment);
        }else{
            ft.replace(R.id.fragment_container, fragment);
        }
        ft.commit();
    }

    @Override
    public void changeFragment(Fragment fragment) {

    }
}