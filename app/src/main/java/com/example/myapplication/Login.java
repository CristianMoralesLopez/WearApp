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
            String id = "";

            try {
                OkHttpClient client = new OkHttpClient();

                RequestBody postData = new FormBody.Builder()
                        .add("email",strings[1]).
                        add("type","0")
                        .add("password",strings[0]).build();

              Request request = new Request.Builder().url("http://ec2-52-4-203-111.compute-1.amazonaws.com:8080/sesion").post(postData).build();
              Response  response = client.newCall(request).execute();

              System.out.println("CODIGO BACK-END "+response.code());
             // JSONObject object1 = new JSONObject(response.body().string());
              //System.out.println(object1.getString("code"));



              if(response.code() == 200){
                  retorno = "ok";

                  JSONObject object = new JSONObject(response.body().string());

                  id = object.getString("id");




              }else if (response.code() == 403){

                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getString("code").equals("auth/invalid-email")){

                        System.out.println("INGRESO CONTRASEÑA MAL");

                        retorno = "user";

                    }else if (object.getString("code").equals("auth/wrong-password")){

                        System.out.println("INGRESO USUARIO MAL");

                        retorno = "password";

                    }


                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            publishProgress(new String [] {retorno,id});
            return null;
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

           login.comprobarLogin(values[0], values[1]);
            
            
        }
    }

    private void comprobarLogin(String retorno, String id) {

        System.out.println("Si ingreso");

        if(retorno.equals("ok")) {

            Intent i = new Intent(Login.this, SetUp.class);
            i.putExtra("id",id);

            startActivity(i);
        }else if (retorno.equals("password")){
            Toast.makeText(this.getApplicationContext(),"Contraseña Incorrecta",Toast.LENGTH_LONG).show();
        }else if (retorno.equals("user")){
            Toast.makeText(this.getApplicationContext(),"el usuario no existe",Toast.LENGTH_LONG).show();
        }

    }


}
