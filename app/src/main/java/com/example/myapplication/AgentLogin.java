package com.example.myapplication;
import android.content.Context;
import androidx.annotation.NonNull;

import org.json.JSONObject;
import java.util.concurrent.TimeUnit;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AgentLogin {


    public AgentLogin(Context context) {

        LocalDataBase.getInstance(context);
    }

    public boolean isSingIn() {
        return LocalDataBase.getInstance(null).getUser() != null;//Log.i("Error: ",(LocalDataBase.getInstance(null).getUser() == null)+"");
    }

    public void signOut() {

        LocalDataBase.getInstance(null).deletedCredentials();
    }

    public void registrar(final String email, final String password, final DefaultCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient okhttp = new OkHttpClient.Builder()
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(5, TimeUnit.SECONDS)
                            .build();

                    RequestBody postData = new FormBody.Builder()
                            .add("email",email).
                                    add("type","0")
                            .add("password",password).build();

                    Request request = new Request.Builder().url("http://ec2-52-4-203-111.compute-1.amazonaws.com:8080/sesion").post(postData).build();
                    Response  response = okhttp.newCall(request).execute();



                    if (response.code() == 200) {

                        JSONObject object = new JSONObject(response.body().string());

                        User user = new User();
                        user.setId(object.getString("id"));
                        user.setEmail(object.getString("email"));
                        user.setName(object.getString("nombre"));
                        user.setPhone((object.getString("telefono")));
                        user.setDepartment((object.getString("departamento")));
                        user.setAdress((object.getString("direccion")));
                        user.setMunicipality((object.getString("municipio")));
                        user.setBirth((object.getString("nacimiento")));

                        LocalDataBase.getInstance(null).saveUser(user);
                        callback.onFinishProcess(true, null);
                    } else
                        callback.onFinishProcess(false, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

}