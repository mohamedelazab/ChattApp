package com.example.mohamed.chattapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mohamed.chattapp.R;
import com.example.mohamed.chattapp.utils.Session;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Session.getInstance().isUserLoggedIn()){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }
        else {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            finish();
        }
    }
}