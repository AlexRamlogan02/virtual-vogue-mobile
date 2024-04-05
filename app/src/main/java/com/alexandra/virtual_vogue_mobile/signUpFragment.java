package com.alexandra.virtual_vogue_mobile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class signUpFragment extends Fragment {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Map<String, String> params;
    OkHttpClient client;
    TextView text;
    EditText fname, lname, email, password, login;
    String fnameS, lnameS, emailS, passwordS, loginS, url;
    JSONObject parameter;
    String TAG = "signupFragment";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create SignUp Fragment");
        View parentView =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        params = new HashMap<String, String>();
        client = new OkHttpClient();
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Register";
        Button button = (Button) parentView.findViewById(R.id.registerButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = (EditText) parentView.findViewById(R.id.createFirstNameInput);
                lname = (EditText) parentView.findViewById(R.id.createLastNameInput);
                email = (EditText) parentView.findViewById(R.id.createEmailInput);
                password = (EditText) parentView.findViewById(R.id.createPasswordInput);
                login = (EditText) parentView.findViewById(R.id.createUsernameInput);

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

        return parentView;
    }

    public void post()
    {
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getActivity(), loginFragment.class));
                    }
                });

            }
        });
    }
}
/*
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
 */