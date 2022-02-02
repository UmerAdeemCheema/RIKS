package com.ua.riks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        User.restart(getApplicationContext());

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                SharedPreferences prefs = getApplicationContext().getSharedPreferences("RIKS_SH_PREF", getApplicationContext().MODE_PRIVATE);
                int abc =  prefs.getInt("USERIDKEY", -1);
                User.id=abc;

                if(abc == -1){
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(MainActivity.this,LoginRegisterActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
                else {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(MainActivity.this,ProfileActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
            }
        }, 2000);
    }
}
