package com.alexandra.virtual_vogue_mobile;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class signupActivity extends AppCompatActivity {

    String url;
    OkHttpClient client;
    JSONObject parameter;
    EditText fname, lname, email, password, login;

    Map<String, String> params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = new OkHttpClient();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        params = new HashMap<String, String>();
        client = new OkHttpClient();
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Register";

        Button buttonRegister = findViewById(R.id.register);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}