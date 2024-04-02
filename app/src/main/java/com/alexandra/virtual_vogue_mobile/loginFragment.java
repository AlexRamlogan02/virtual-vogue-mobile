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
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class loginFragment extends Fragment {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Map<String, String> params;
    OkHttpClient client;
    TextView text;
    String url;
    String TAG = "loginFragment";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Called");
        View parentView =  inflater.inflate(R.layout.fragment_login, container, false);

        //place listeners on the edit boxes!
        Button buttonLogin = getView().findViewById(R.id.loginButton);


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

 */