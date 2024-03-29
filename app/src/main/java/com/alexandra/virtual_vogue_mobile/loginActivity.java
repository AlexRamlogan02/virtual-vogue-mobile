package com.alexandra.virtual_vogue_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class loginActivity extends AppCompatActivity {

    TextView text;
    OkHttpClient client;
    String url;
    EditText etLogin, etPassword;
    String login;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        client = new OkHttpClient();
        text = findViewById(R.id.titleText);
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Login";
        etLogin = findViewById(R.id.usernameInput);
        etPassword = findViewById(R.id.passwordInput);
        login = etLogin.getText().toString();
        password = etPassword.getText().toString();

        Button buttonLogin = findViewById(R.id.loginButton);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }

        });
    }

    public void post(){
        RequestBody requestBody = new FormBody.Builder()
                .add("Login", login)
                .add("Password", password)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                text.setText("no");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    text.setText("correct");
                }
                else {
                    text.setText("wrong");
                }
            }
        });

    }
}