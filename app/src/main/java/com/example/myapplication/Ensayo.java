package com.example.myapplication;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ensayo extends WearableActivity {

    private TextView mTextView;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensayo);

        mTextView = (TextView) findViewById(R.id.text);

        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference();



        final DatabaseReference referencia =  databaseReference.child("pacientes").child("b4Suc3zhlPbR3LJyy7QY7vGtHUQ2").child("monitoreo").child("pulso").child("tomas");

        referencia.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println("Valor Hijo"+ dataSnapshot.getChildrenCount());

                System.out.println("valor Nodo Final"+(dataSnapshot.getChildrenCount()+1));

                System.out.println(dataSnapshot.getValue().toString());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Enables Always-on
        setAmbientEnabled();
    }
}
