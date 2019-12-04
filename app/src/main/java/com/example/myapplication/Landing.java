package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;



public class Landing extends WearableActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {





                    Intent intent = new Intent(Landing.this, Login.class);
                    startActivity(intent);

            }
        }, 3000);




    }
}