package com.example.aplikacja.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aplikacja.R;

public class IntroActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    Animation topAnim, bottomAnim;

    ImageView imageView;

    TextView title, desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        imageView = findViewById(R.id.intro_image);
        title = findViewById(R.id.intro_title);
        desc = findViewById(R.id.intro_text_desc);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_navigation);

        imageView.setAnimation(topAnim);
        title.setAnimation(bottomAnim);
        desc.setAnimation(bottomAnim);



        sharedPref = getSharedPreferences("SharedPref", MODE_PRIVATE);
        boolean isFirstTime = sharedPref.getBoolean("firstTime", true);

        if (isFirstTime){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, 4000);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("firstTime", false);
            editor.apply();
        }else{
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}