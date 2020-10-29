package com.example.realestate.Activities;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import com.example.realestate.R;


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
        loadAnimation = (AnimationDrawable) loadImage.getBackground();
        loadAnimation.start();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadAnimation.stop();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                MainActivity.this.finish();
            }
        }, 4000);

    }

}