package com.example.myapplication;




import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Monitoring extends WearableActivity implements SensorEventListener {

    private final static int SENS_HEARTRATE = Sensor.TYPE_HEART_RATE;

    private SensorManager sensorManager;
    private int valorPasos;
    private boolean valorInicialStepCounter;
    private int stepCounterValue;
    private Sensor mHeartrateSensor;
    private Sensor mStepCountSensor;
    private ScheduledExecutorService mScheduler;
    private ArrayList<Integer> valoresPulso;
    private String horaInicio;
    private String horaFin;
    private CalcualdoraCalorias calcualdoraCalorias;


    private Chronometer chronometer;
    private TextView lblPulso;
    private TextView lblPasos;
    private ImageButton btnStop;
    private boolean isResume;
    private Handler handler;
    private String duracion;
    long tMillliSec, tStart, tBuff, tUpdate= 0L;
    int sec, min, milliSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        Bundle bundle = this.getIntent().getExtras();
        stepCounterValue = 0;
        valorInicialStepCounter = true;
        duracion = (String) bundle.get("minutos");
        valoresPulso = new ArrayList<>();
        chronometer = findViewById(R.id.cronometro);
        btnStop = findViewById(R.id.btnFinalizar);
        lblPulso = findViewById(R.id.lblPulso);
        lblPasos = findViewById(R.id.lblPasosMonitoring);
        sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mHeartrateSensor = sensorManager.getDefaultSensor(SENS_HEARTRATE);
        mStepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        handler = new Handler();
        tStart = SystemClock.uptimeMillis();
        handler.postDelayed(runnable,0);
        chronometer.start();
        supervisorTiempo();
        sensorManager.registerListener(Monitoring.this, mStepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);


        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Monitoring.this, chronometer.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


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

            int paradaCronometro = Integer.parseInt(duracion);

            if (paradaCronometro == min){
                Toast.makeText(getApplicationContext(), "tiempo cumplido",Toast.LENGTH_LONG).show();
            }else {
                System.out.println(""+ paradaCronometro );
            }
            chronometer.setText(minutos + ":"+  segundos);

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        final int measurementDuration   = Integer.parseInt(duracion)*60;   // Seconds
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

        if(event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            final String msg = "" + (int) event.values[0];
            valoresPulso.add((int) event.values[0]);
            lblPulso.setText(msg);

            if ((int) event.values[0] >= 100) {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] vibrationPattern = {0, 500, 50, 300};
                final int indexInPatternToRepeat = -1;
                vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
                Toast.makeText(getApplicationContext(), "Bajar la intesidad del ejercicio " + "\n" + "su pulso a sobrepasado el limite" + "\n" + msg + " BPM",
                        Toast.LENGTH_LONG).show();
            }
        }

        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){

            if (valorInicialStepCounter){
                stepCounterValue = (int) event.values[0];
                valorInicialStepCounter= false;
            }

            valorPasos = ((int)event.values[0]) - stepCounterValue;

            final String msg = "" + (int) valorPasos;
            lblPasos.setText(msg);



        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void supervisorTiempo(){

        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep((2000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean parada = true;
                String cadenaHora = "";
                int comparacion = 0;
                while(parada){

                    cadenaHora = chronometer.getText().toString();
                    System.out.println(cadenaHora);
                    cadenaHora = ""+cadenaHora.charAt(0)+cadenaHora.charAt(1);
                    if ((""+cadenaHora.charAt(0)).equals("0")){
                        cadenaHora = ""+cadenaHora.charAt(1);
                        comparacion = Integer.parseInt(cadenaHora);
                    }else{
                        comparacion = Integer.parseInt(cadenaHora);
                    }

                    int paradaCronometro = Integer.parseInt(duracion);

                    if (paradaCronometro == comparacion){
                        horaFin = hora();

                        int valorSegundor = Integer.parseInt(duracion)*60;

                        CalcualdoraCalorias calcualdoraCalorias = new CalcualdoraCalorias();

                        float calorias = calcualdoraCalorias.calculateEnergyExpenditure((float)1.7,56,0,valorSegundor, valorPasos,(float)0.3);

                        String caloriasBundle = "" + calorias;


                        Collections.sort(valoresPulso);



                        Rutina rutina = new Rutina();
                        rutina.setValoresPulso(valoresPulso);
                        rutina.setDuracion(duracion);
                        rutina.setHoraInicio(horaInicio);
                        rutina.setHoraFin(horaFin);
                        rutina.setValoresPulso2(null);
                        rutina.setCalorias(caloriasBundle);
                        rutina.setPasos(""+valorPasos);
                        rutina.setMenorPulso(""+valoresPulso.get(0));
                        rutina.setMayorPulso(""+valoresPulso.get(valoresPulso.size()-1));
                        rutina.setPromedioPulso(promedioPulso(valoresPulso));
                        parada = false;


                        Intent i = new Intent(Monitoring.this, ResumenActividades.class);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("rutina",rutina);
                        bundle.putString("valorPulso", promedioPulso(valoresPulso));
                        bundle.putString("duracion",duracion);
                        bundle.putString("horaInicio",horaInicio);
                        bundle.putString("horaFin",horaFin);
                        bundle.putString("pasos",""+valorPasos);
                        bundle.putString("calorias",caloriasBundle);

                        i.putExtras(bundle);

                        mScheduler.shutdown();
                        unregisterListener();
                        startActivity(i);
                        finish();
                    }

                }

            }
        });
        hilo.start();
    }

    private String hora (){

        Calendar calendario = Calendar.getInstance();
        int hora, minutos, segundos;

        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);

        String retorno = hora + ":" + minutos + ":" + segundos;

        return retorno;
    }

    private void unregisterListener(){
        sensorManager.unregisterListener(this);
    }


    public String promedioPulso (ArrayList<Integer> valores){

        int acumulado = 0;



        for (int i = 0; i < valores.size();i++ ){

            acumulado += valores.get(i);
        }


        return ""+ (acumulado/valores.size());
    }





    @Override
    protected void onStart() {
        super.onStart();
        horaInicio = hora();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScheduler.shutdown();
        unregisterListener();
    }
}
