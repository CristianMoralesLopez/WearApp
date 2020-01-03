package com.example.myapplication;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Database  {


    private FirebaseDatabase firebaseDatabase;


    public Database(){

        firebaseDatabase = FirebaseDatabase.getInstance();

    }


    public void envioInformacion(final Rutina rutina){



        Calendar calendario = Calendar.getInstance();

        final int intHora, intMinutos, intSegundos,intAño,intMes,intDia;

        intHora =calendario.get(Calendar.HOUR_OF_DAY);
        intMinutos = calendario.get(Calendar.MINUTE);
        intSegundos = calendario.get(Calendar.SECOND);
        intAño = calendario.get(Calendar.YEAR) ;
        intMes = calendario.get(Calendar.MONTH) + 1;
        intDia = calendario.get(Calendar.DAY_OF_MONTH);






        String segundos, minutos,hora;

        if (intHora>0 && intHora<10){
            hora= "0"+intHora;
        }else{

            hora= ""+intHora;
        }

        if (intMinutos>0 && intMinutos<10){
            minutos= "0"+intMinutos;
        }else{

           minutos= ""+intMinutos;
        }
        if (intSegundos>0 && intSegundos<10){
           segundos= "0"+intSegundos;
        }else{

            segundos= ""+intSegundos;
        }


        final String tiempo = (hora + ":" + minutos + ":" + segundos);

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("pacientes").child(LocalDataBase.getInstance(null).getUser().getUID()).child("monitoreo").child("pulso").child(""+intAño).child(""+intMes)
                .child(""+intDia).child(tiempo).setValue(rutina);

       final DatabaseReference referencia =  databaseReference.child("pacientes").child(LocalDataBase.getInstance(null).getUser().getUID()).child("monitoreo").child("pulso").child("tomas");

        referencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println("valores de las tomas" +
                        ""+dataSnapshot.getChildrenCount());


                referencia.child(""+dataSnapshot.getChildrenCount()).child("fecha").setValue(intAño+"/"+intMes+"/"+intDia+"/"+tiempo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference referenciaPasosLogrados =  databaseReference.child("pacientes").child(LocalDataBase.getInstance(null).getUser().getUID()).child("metas").child("PasosLogrados");

        referenciaPasosLogrados.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              String valorActual = (String)  dataSnapshot.getValue().toString();

              int valorSumar = Integer.parseInt(rutina.getPasos());

              System.out.println("cantidad de calorias" + rutina.getCalorias());

              int valoActualint = Integer.parseInt(valorActual);

              int valorAñadir = valorSumar + valoActualint;

              referenciaPasosLogrados.setValue(""+ valorAñadir);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference referenciaKgLogrados =  databaseReference.child("pacientes").child(LocalDataBase.getInstance(null).getUser().getUID()).child("metas").child("kgCaloriasLogradas");

        referenciaKgLogrados.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String valorActual = (String)  dataSnapshot.getValue().toString();


                double valorSumar = Double.parseDouble(rutina.getCalorias());

                System.out.println("cantidad de calorias" + rutina.getCalorias());

                Double valoActualint = Double.parseDouble(valorActual);

                double valorAñadir = valorSumar + valoActualint;

                referenciaKgLogrados.setValue(""+ valorAñadir);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        try {

            OkHttpClient okhttp = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();


            RequestBody body = new FormBody.Builder()
                    .add("id", LocalDataBase.getInstance(null).getUser().getUID())
                    .add("data", "dkflañs")

                    .build();

            Request request = new Request.Builder()
                    .url(NetworkConstants.URL + NetworkConstants.PATH_FINIS_SESION)
                    .post(body)
                    .build();

            Response response = okhttp.newCall(request).execute();

        }catch (Exception e){


        }






    }

}
