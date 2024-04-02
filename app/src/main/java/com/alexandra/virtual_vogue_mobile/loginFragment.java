package com.alexandra.virtual_vogue_mobile;

import androidx.lifecycle.ViewModelProvider;

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

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class loginFragment extends Fragment {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Map<String, String> params ;
    OkHttpClient client;
    TextView text;
    EditText eLogin, ePass;
    String url, login, password;
    String TAG = "loginFragment";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Called");
        View parentView =  inflater.inflate(R.layout.fragment_login, container, false);

        params = new HashMap<String, String>();
        client = new OkHttpClient();
        text = parentView.findViewById(R.id.loginText);
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Login";
        Button button = (Button) parentView.findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eLogin = (EditText) parentView.findViewById(R.id.usernameInput);
                ePass = (EditText) parentView.findViewById(R.id.passwordInput);
                login = eLogin.toString();
                password = ePass.toString();
                text.setText("bruh");
            }
        });


        return parentView;
    }
    private void onButtonClick(View view){
        Log.d(TAG, "onButtonClick: Started");

        //send login request!
    }

}

/*
params = new HashMap<String, String>();
        client = new OkHttpClient();
        text = getView().findViewById(R.id.loginText);
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Login";
        Button buttonLogin = getView().findViewById(R.id.loginButton);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        text.setText("wrong/password");
        }
        });

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
 */