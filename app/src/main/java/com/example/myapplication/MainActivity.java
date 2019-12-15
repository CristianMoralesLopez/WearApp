package com.example.myapplication;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private final static int SENS_HEARTRATE = Sensor.TYPE_HEART_RATE;

    private TextView mTextView;
    private SensorManager sensorManager;
    private Sensor mHeartrateSensor;
    private ScheduledExecutorService mScheduler;
    private Database database;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle =  getIntent().getExtras();

        id = bundle.getString("id");

       mTextView = (TextView) findViewById(R.id.text);


       mTextView.setText("Valor ritmo cardiaco:");

        sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));

        mHeartrateSensor = sensorManager.getDefaultSensor(SENS_HEARTRATE);

        database = new Database();


        // Enables Always-on
        setAmbientEnabled();
    }


    @Override
    protected void onResume() {
        super.onResume();
        final int measurementDuration   = 15;   // Seconds
        final int measurementBreak      = 10;    // Seconds

        mScheduler = Executors.newScheduledThreadPool(1);
        mScheduler.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {


                       sensorManager.registerListener(MainActivity.this, mHeartrateSensor, SensorManager.SENSOR_DELAY_FASTEST);

                        try {
                            Thread.sleep(measurementDuration * 1000);

                        } catch (InterruptedException e) {

                        }

                        sensorManager.unregisterListener(MainActivity.this, mHeartrateSensor);
                    }
                }, 3, measurementDuration + measurementBreak, TimeUnit.SECONDS);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
            final String msg = "Ritmo cardiaco:"+ "\n" + (int)event.values[0] ;

           mTextView.setText(msg);

         Thread hiloDatabase = new Thread(new Runnable() {
            public void run() {


            }
        });


         hiloDatabase.start();
            }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
