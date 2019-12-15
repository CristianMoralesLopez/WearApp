package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;



public class Landing extends WearableActivity {

    private AgentLogin agentLogin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing);
        agentLogin = new AgentLogin(this);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {




                if (agentLogin.isSingIn()){
                    Intent intent = new Intent(Landing.this, SetUp.class);
                    startActivity(intent);
                }else {

                    Intent intent = new Intent(Landing.this, Login.class);
                    startActivity(intent);
                }

                finish();

            }
        }, 3000);




    }
}