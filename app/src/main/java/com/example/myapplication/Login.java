package com.example.myapplication;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends WearableActivity implements DefaultCallback{
    private Login login = this;
    private EditText txtCorreo;
    private EditText txtContraseña;
    private Button btnLogin;
    private AgentLogin agentLogin;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContraseña = findViewById(R.id.txtContraseña);
        btnLogin = findViewById(R.id.btnIngresar);
        agentLogin = new AgentLogin(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loguearUsuario();
            }
        });
    }

    public void loguearUsuario(){

       final String email = txtCorreo.getText().toString().trim();
       final String password = txtContraseña.getText().toString().trim();

        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Se debe de ingresar un email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(),"Se debe de ingresar una contraseña",Toast.LENGTH_LONG).show();
            return;
        }
        agentLogin.registrar(email,password,this);
    }

    @Override
    public void onFinishProcess(boolean hasSucceeded, Object result) {
             comprobarLogin(hasSucceeded);
              }



    private void comprobarLogin(boolean retorno) {

        System.out.println("Si ingreso");

        if(retorno) {

            Intent i = new Intent(Login.this, SetUp.class);
            startActivity(i);
            finish();
        }else {
            Toast.makeText(this.getApplicationContext(),"Contraseña Incorrecta o correo Incorrecto",Toast.LENGTH_LONG).show();
        }

    }


}
