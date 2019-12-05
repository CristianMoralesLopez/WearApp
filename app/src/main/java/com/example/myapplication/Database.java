package com.example.myapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class Database  {


    private FirebaseDatabase firebaseDatabase;

    public Database(){

        firebaseDatabase = FirebaseDatabase.getInstance();

    }


    public void envioInformacion(int valorPromedio, String id){



        Calendar calendario = Calendar.getInstance();

        int intHora, intMinutos, intSegundos,intAño,intMes,intDia;

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


        String tiempo = (hora + ":" + minutos + ":" + segundos);

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("pacientes").child(id).child("monitoreo").child("pulso").child(""+intAño).child(""+intMes)
                .child(""+intDia).child(tiempo).setValue(valorPromedio);



    }

}
