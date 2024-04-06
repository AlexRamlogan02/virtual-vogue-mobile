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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;

public class loginFragment extends Fragment {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Map<String, String> params;
    OkHttpClient client;
    TextView text;
    EditText eLogin, ePass;
    String url, login, password;
    JSONObject parameter;
    String TAG = "loginFragment";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Called");
        View parentView =  inflater.inflate(R.layout.fragment_login, container, false);

        params = new HashMap<String, String>();
        client = new OkHttpClient();
        text = parentView.findViewById(R.id.loginResult);
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Login";
        Button button = (Button) parentView.findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onButtonClick: Started");
                eLogin = (EditText) parentView.findViewById(R.id.usernameInput);
                ePass = (EditText) parentView.findViewById(R.id.passwordInput);
                login = eLogin.getText().toString();
                password = ePass.getText().toString();
                params.put("login", login);
                params.put("password", password);
                parameter = new JSONObject(params);
                post();
            }
        });


        return parentView;
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

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text.setVisibility(View.VISIBLE);
                                text.setText("wrong password");
                            }
                        });

                    }
                    else{
                        startActivity(new Intent(getActivity(), landingPage.class));
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: Error", e);
                    System.exit(1); //exit system on failure
                    //could run a toast but i dont wanna
                }

            }
        });
    }
}