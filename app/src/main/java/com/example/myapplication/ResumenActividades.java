package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MotionEventCompat;

import android.support.wearable.activity.WearableActivity;
import android.support.wearable.activity.WearableActivityDelegate;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class ResumenActividades extends WearableActivity {

    private TextView lblPulsoPromedio;
    private TextView lblHoraIncio;
    private TextView lblDuracion;
    private TextView lblHoraFin;
    private ImageButton btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_actividades);
        lblPulsoPromedio = findViewById(R.id.lblPulsoPromedio);
        lblDuracion = findViewById(R.id.lblduracion);
        lblHoraIncio = findViewById(R.id.lblHoraInicio);
        lblHoraFin = findViewById(R.id.lblHoraFin);

        btnSend = findViewById(R.id.btnSend);

        Bundle bundle = getIntent().getExtras();

        lblPulsoPromedio.setText(bundle.getString("valorPulso"));
        lblDuracion.setText(bundle.getString("duracion"));
        lblHoraIncio.setText(bundle.getString("horaInicio"));
        lblHoraFin.setText(bundle.getString("horaFin"));


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResumenActividades.this, SetUp.class);
                startActivity(i);
                finish();
            }
        });

    }



}
