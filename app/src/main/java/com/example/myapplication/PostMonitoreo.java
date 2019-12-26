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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PostMonitoreo extends WearableActivity implements SensorEventListener {

    private final static int SENS_HEARTRATE = Sensor.TYPE_HEART_RATE;

    private SensorManager sensorManager;
    private Sensor mHeartrateSensor;
    private ScheduledExecutorService mScheduler;
    private ArrayList<Integer> valoresPulso;
    private String horaInicio;
    private String horaFin;
    private Rutina rutina;


    private Chronometer chronometer;
    private TextView lblPulso;
    private ImageButton btnStop;
    private boolean isResume;
    private Handler handler;
    private String duracion;
    long tMillliSec, tStart, tBuff, tUpdate= 0L;
    int sec, min, milliSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_monitoreo);
        Bundle bundle = this.getIntent().getExtras();
        duracion = (String) "1";
        valoresPulso = new ArrayList<>();
        chronometer = findViewById(R.id.Pcronometro);
        btnStop = findViewById(R.id.PbtnFinalizar);
        lblPulso = findViewById(R.id.PlblPulso);
        sensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mHeartrateSensor = sensorManager.getDefaultSensor(SENS_HEARTRATE);
        handler = new Handler();
        tStart = SystemClock.uptimeMillis();
        handler.postDelayed(runnable,0);
        chronometer.start();
        supervisorTiempo();

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostMonitoreo.this, chronometer.getText().toString(), Toast.LENGTH_SHORT).show();
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


                        sensorManager.registerListener(PostMonitoreo.this, mHeartrateSensor, SensorManager.SENSOR_DELAY_FASTEST);

                        try {
                            Thread.sleep(measurementDuration * 1000);

                        } catch (InterruptedException e) {

                        }

                        sensorManager.unregisterListener(PostMonitoreo.this, mHeartrateSensor);
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
        valoresPulso.add((int   ) event.values[0]);
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
                       rutina.setHoraFin1(hora());
                       rutina.setValoresPulso2(valoresPulso);


                        parada = false;

                        Database database = new Database();
                        database.envioInformacion(rutina);

                        Intent i = new Intent(PostMonitoreo.this, SetUp.class);
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

    @Override
    protected void onStart() {
        super.onStart();
        final Bundle bundle = getIntent().getExtras();

        rutina = (Rutina) bundle.getSerializable("rutina");

        rutina.setHoraInicio1(hora());
    }
}
