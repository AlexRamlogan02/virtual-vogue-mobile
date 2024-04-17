package com.alexandra.virtual_vogue_mobile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    EditText fname, lname, email, password, login, matchPass;
    String fnameS, lnameS, emailS, passwordS, loginS, matchS, url;
    JSONObject parameter;
    String TAG = "signupFragment";
    boolean signUpError;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create SignUp Fragment");
        View parentView =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        params = new HashMap<String, String>();
        client = new OkHttpClient();
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Register";
        text = parentView.findViewById(R.id.signupErrorMessage);
        Button button = (Button) parentView.findViewById(R.id.registerButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpError = false;
                text.setVisibility(View.INVISIBLE);

                String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
                Pattern pattern = Pattern.compile(regex);

                fname = (EditText) parentView.findViewById(R.id.createFirstNameInput);
                lname = (EditText) parentView.findViewById(R.id.createLastNameInput);
                email = (EditText) parentView.findViewById(R.id.createEmailInput);
                password = (EditText) parentView.findViewById(R.id.createPasswordInput);
                login = (EditText) parentView.findViewById(R.id.createUsernameInput);
                matchPass = (EditText) parentView.findViewById(R.id.confirmPasswordInput);

                fnameS = fname.getText().toString();
                lnameS = lname.getText().toString();
                emailS = email.getText().toString();
                passwordS = password.getText().toString();
                matchS = matchPass.getText().toString();
                loginS = login.getText().toString();

                Matcher matcher = pattern.matcher(emailS);

                params.put("login", loginS);
                params.put("password", passwordS);
                params.put("firstName", fnameS);
                params.put("lastName", lnameS);
                params.put("email", emailS);

                parameter = new JSONObject(params);

                if (fnameS.isEmpty()) {
                    signUpError = true;
                    text.setText("First Name required");
                    text.setVisibility(View.VISIBLE);
                } else if (lnameS.isEmpty()) {
                    signUpError = true;
                    text.setText("Last Name required");
                    text.setVisibility(View.VISIBLE);
                } else if (!matcher.matches() || emailS.isEmpty()) {
                    signUpError = true;
                    text.setText("Invalid Email");
                    text.setVisibility(View.VISIBLE);
                } else if (loginS.isEmpty()) {
                    signUpError = true;
                    text.setText("Username required");
                    text.setVisibility(View.VISIBLE);
                } else if (passwordS.isEmpty()) {
                    signUpError = true;
                    text.setText("Password must be 8 characters long, one upper and lower case letter, and a special symbol");
                    text.setVisibility(View.VISIBLE);
                } else if (!passwordS.matches(matchS)) {
                    signUpError = true;
                    text.setText("Passwords don't match");
                    text.setVisibility(View.VISIBLE);
                }

                if (!signUpError) {
                    post();
                }

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


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String json = response.body().string();
                            JSONObject jobj = new JSONObject(json);

                            String error = jobj.getString("error");
                            if (error.matches("")) {
                                text.setText("Verification email has been sent!");
                                text.setVisibility(View.VISIBLE);
                                text.setTextColor(getResources().getColor(R.color.reseda_green));

                            } else {
                                text.setText(error);
                                text.setVisibility(View.VISIBLE);
                                text.setTextColor(getResources().getColor(R.color.blush));
                            }
                        } catch (JSONException e){

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });


            }
        });
    }
}
