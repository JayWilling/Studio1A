package com.example.studiomerge;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    private static final int timeOut = 2000;

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        logo = findViewById(R.id.logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);

                finish();
            }
        }, timeOut);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        logo.startAnimation(animation);
    }
}

