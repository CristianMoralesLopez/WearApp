package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;

public class SetUp extends WearableActivity {

    private Button btnIniciar;
    private Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        btnIniciar = findViewById(R.id.btnRutina);
        btnSignOut = findViewById(R.id.btnSignout);


        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SetUp.this,TimeSetUp.class);
                startActivity(i);
                finish();

            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AgentLogin agentLogin = new AgentLogin(v.getContext());

                agentLogin.signOut();

                Intent i = new Intent(SetUp.this, Login.class);

                startActivity(i);

                finish();


            }
        });

    }
}
