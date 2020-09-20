package com.example.realestate;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView loadImage;
    private AnimationDrawable loadAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getSupportActionBar().hide();

        loadImage = findViewById(R.id.loadID);
        loadImage.setBackgroundResource(R.drawable.load_animation);
        loadAnimation = (AnimationDrawable)loadImage.getBackground();
        loadAnimation.start();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadAnimation.stop();
                MainActivity.this.finish();
            }
        },4000);

    }
}