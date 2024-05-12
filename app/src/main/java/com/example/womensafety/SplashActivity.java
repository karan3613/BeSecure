package com.example.womensafety;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        ActionBar actionBar ;
        actionBar =getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F03333"));
        actionBar.setBackgroundDrawable(colorDrawable);
        imageView = findViewById(R.id.imageView);

   new Handler().postDelayed((new Runnable() {
       @Override
       public void run() {
           Intent home = new Intent(SplashActivity.this , MainActivity.class);
           startActivity(home);
        finish();
       }
   }) , 2000) ;


    }
}