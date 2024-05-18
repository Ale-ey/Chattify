package com.example.chatify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    TextView ts_ali, ts_arslan,ts_chatify;
    ImageView c_logo;

    Animation topanim, bottomanim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
//        getSupportActionBar().hide();    //cause error


        c_logo= findViewById(R.id.logo_chatify);
        ts_ali= findViewById(R.id.text_ali);
        ts_arslan= findViewById(R.id.text_arslan);
        ts_chatify= findViewById(R.id.text_chatify);

        topanim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        c_logo.setAnimation(topanim);
        ts_chatify.setAnimation(bottomanim);
        ts_ali.setAnimation(bottomanim);
        ts_arslan.setAnimation(bottomanim);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(splash.this, login.class);
            startActivity(intent);
            finish();
        },4000);

    }

}