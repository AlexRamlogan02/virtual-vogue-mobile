package com.alexandra.virtual_vogue_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loginActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    TextView text;
    OkHttpClient client;
    String url;
    EditText etLogin, etPassword;
    String login;
    String password;
    JSONObject parameter;
    Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        params = new HashMap<String, String>();

        client = new OkHttpClient();
        text = findViewById(R.id.titleText);
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Login";




        Button buttonLogin = findViewById(R.id.loginButton);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etLogin = (EditText) findViewById(R.id.usernameInput);
                etPassword = (EditText) findViewById(R.id.passwordInput);
                login = etLogin.getText().toString();
                password = etPassword.getText().toString();
                params.put("login", login);
                params.put("password", password);
                parameter = new JSONObject(params);
                post();
            }

        });
    }

    public void post(){



        RequestBody body = RequestBody.create(JSON, parameter.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {

                    String user = "";
                    String json = response.body().string();
                    JSONObject jobj = new JSONObject(json);

                    user = jobj.getString("userId");


                    if (user.startsWith("-1")){
                        loginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setText("wrong/password");
                            }
                        });
                    }
                    else{
                        loginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(loginActivity.this, MainActivity.class));
                            }
                        });
                    }

                } catch (JSONException e) {

                }

            }
        });

    }
}