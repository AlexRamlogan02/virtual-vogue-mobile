package com.alexandra.virtual_vogue_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class signupActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    String url;
    TextView text;
    OkHttpClient client;
    JSONObject parameter;
    EditText fname, lname, email, password, login;
    String fnameS, lnameS, emailS, passwordS, loginS;

    Map<String, String> params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = new OkHttpClient();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        params = new HashMap<String, String>();
        client = new OkHttpClient();
        text = findViewById(R.id.title);
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Register";

        Button buttonRegister = findViewById(R.id.register);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = (EditText) findViewById(R.id.createFirstNameInput);
                lname = (EditText) findViewById(R.id.createLastNameInput);
                email = (EditText) findViewById(R.id.createEmailInput);
                password = (EditText) findViewById(R.id.createPasswordInput);
                login = (EditText) findViewById(R.id.createUsernameInput);

                fnameS = fname.getText().toString();
                lnameS = lname.getText().toString();
                emailS = email.getText().toString();
                passwordS = password.getText().toString();
                loginS = login.getText().toString();


                params.put("login", loginS);
                params.put("password", passwordS);
                params.put("firstName", fnameS);
                params.put("lastName", lnameS);
                params.put("email", emailS);

                parameter = new JSONObject(params);
                post();
            }
        });
    }

    public void post() {
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
                    String json = response.body().string();
                    //JSONObject jobj = new JSONObject(json);
                        signupActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(signupActivity.this, loginActivity.class));
                            }
                        });



            }
        });

    }
}