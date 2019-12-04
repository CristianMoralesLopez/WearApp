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
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends WearableActivity {
    private Login login = this;
    private EditText txtCorreo;
    private EditText txtContraseña;
    private Button btnLogin;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContraseña = findViewById(R.id.txtContraseña);
        btnLogin = findViewById(R.id.btnIngresar);

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

        ClientHttp hiloResultadco = new ClientHttp();
        hiloResultadco.execute(new String[]{password, email});



    }

    private class ClientHttp extends AsyncTask <String, String,String> {

        @Override
        protected String doInBackground(String... strings) {

            String retorno = "";

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody postData = new FormBody.Builder()
                        .add("correo",strings[0])
                        .add("contraseña",strings[1]).build();

                Request request = new Request.Builder().url("http://ec2-52-4-203-111.compute-1.amazonaws.com:8080/hello").get().build();
              Response  response = client.newCall(request).execute();
               retorno = response.body().string();
                System.out.println(retorno);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            publishProgress(retorno);
            return null;
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

           login.comprobarLogin(values[0]);
            
            
        }
    }

    private void comprobarLogin(String retorno) {

        Intent i = new Intent(Login.this, MainActivity.class);

        startActivity(i);

    }


}
