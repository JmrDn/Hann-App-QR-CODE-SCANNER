package com.example.hannappqrcodescanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
@RequiresApi(api = Build.VERSION_CODES.O)
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                UserDetails userDetails = new UserDetails(SplashScreen.this);
                String email = userDetails.getEmail();
                String password = userDetails.getPassword();

                if (email != null || password != null){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else{
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }

            }
        }, 2000);
    }
}