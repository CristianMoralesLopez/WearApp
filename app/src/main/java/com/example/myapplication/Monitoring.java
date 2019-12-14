package com.example.myapplication;



import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Monitoring extends WearableActivity implements SensorEventListener {

    private final static int SENS_HEARTRATE = Sensor.TYPE_HEART_RATE;

    private SensorManager sensorManager;
    private Sensor mHeartrateSensor;
    private ScheduledExecutorService mScheduler;


    private Chronometer chronometer;
    private TextView lblPulso;
    private ImageButton btnStop;
    private boolean isResume;
    private Handler handler;
    long tMillliSec, tStart, tBuff, tUpdate= 0L;
    int sec, min, milliSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        chronometer = findViewById(R.id.cronometro);
        btnStop = findViewById(R.id.btnFinalizar);
        lblPulso = findViewById(R.id.lblPulso);
        sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mHeartrateSensor = sensorManager.getDefaultSensor(SENS_HEARTRATE);
        handler = new Handler();
        tStart = SystemClock.uptimeMillis();
        handler.postDelayed(runnable,0);
        chronometer.start();
        setAmbientEnabled();


    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tMillliSec = SystemClock.uptimeMillis() - tStart;
            tUpdate = tBuff - tMillliSec;
            sec = (int) (tUpdate/1000);
            min = (sec/60) *(-1);
            sec = (sec % 60) * -1;
            milliSec= (int) (tUpdate%100);

            String minutos ="";
            String segundos = "";
            if (sec >0 && sec < 10){
                segundos = "0"+sec;
            }
            if(min > 0 && min < 10){
                minutos = "0" + min;
            }




            chronometer.setText(minutos + ":"+  segundos);
          //  handler.postDelayed(this,60);
        }
    };

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


                        sensorManager.registerListener(Monitoring.this, mHeartrateSensor, SensorManager.SENSOR_DELAY_FASTEST);

                        try {
                            Thread.sleep(measurementDuration * 1000);

                        } catch (InterruptedException e) {

                        }

                        sensorManager.unregisterListener(Monitoring.this, mHeartrateSensor);
                    }
                }, 3, measurementDuration + measurementBreak, TimeUnit.SECONDS);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final String msg = ""+(int)event.values[0] ;
        lblPulso.setText(msg);

        if((int)event.values[0]>=100) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] vibrationPattern = {0, 500, 50, 300};
            final int indexInPatternToRepeat = -1;
            vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
            Toast.makeText(getApplicationContext(),"Bajar la intesidad del ejercicio " +"\n"+ "su pulso a sobrepasado el limite"+ "\n" + msg+ " BPM",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
