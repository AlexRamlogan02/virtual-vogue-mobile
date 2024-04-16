package com.alexandra.virtual_vogue_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
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

public class landingSettingsFragment extends Fragment {

    Button makeChangesButton;
    Button saveChangesButton;
    Button deleteAccountButton;
    LinearLayout defaultSettingsView;
    LinearLayout editSettingsView;
    TextView firstNameTV;
    TextView lastNameTV;
    TextView emailTV;
    TextView usernameTV;
    TextView passwordTV;
    EditText firstNameE;
    EditText usernameE;
    EditText lastNameE;
    TextInputEditText passwordE;
    EditText emailE;
    SharedPreferences sharedPreferences;
    String firstName;
    String lastName;
    String email;
    String username;
    String password;
    String userID;
    String TAG = "settingsFragment";
    String url;
    OkHttpClient client;
    JSONObject parameter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_landing_settings, container, false);

        //Initialize Fragment
        try {
            getUserInformation();
            initializeSettings(parentView);
            initializeButtons(parentView);
            initializeEditText(parentView);
            connectToHTTP();
        } catch(Exception e){
            Log.e(TAG, "onCreateView: Error Starting View Information", e);
        }
        return parentView;
    }


    public void getUserInformation() {
        //Retrieve all information saved in sharedPreferences

        sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String defValue = "ERR";
        firstName = sharedPreferences.getString("firstName", defValue);
        lastName = sharedPreferences.getString("lastName", defValue);
        email = sharedPreferences.getString("email", defValue);
        username = sharedPreferences.getString("username", defValue);
        password = sharedPreferences.getString("password", defValue);
        userID = sharedPreferences.getString("user", defValue);
        Log.d(TAG, "getUserInformation: " + firstName + " " + lastName + " " + lastName + " " + email);

    }

    public void initializeSettings(View parentView){
        defaultSettingsView = parentView.findViewById(R.id.defaultSettingsView);
        editSettingsView = parentView.findViewById(R.id.editSettingsLayout);

        //ensure that the correct layout is visible
        if(defaultSettingsView.getVisibility() == View.GONE){
            //hide settings
            editSettingsView.setVisibility(View.GONE);
            defaultSettingsView.setVisibility(View.VISIBLE);
        }
        //find text views
        firstNameTV = parentView.findViewById(R.id.settingsFirstName);
        lastNameTV = parentView.findViewById(R.id.settingsLastName);
        emailTV = parentView.findViewById(R.id.settingsEmail);
        passwordTV = parentView.findViewById(R.id.settingsPassword);
        usernameTV = parentView.findViewById(R.id.settingsUsername);

        //change textViewValues
        String display = "First Name: " + firstName;

        firstNameTV.setText(display);

        display = "Last Name: " + lastName;
        lastNameTV.setText(lastName);
        display = "email: " + email;
        emailTV.setText(email);
        //disguise the password
        StringBuilder tempPass = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            if(i < password.length() % 2)
                tempPass.append(password.charAt(i));
            tempPass.append("*");
        }
        display = "Password: " + tempPass.toString();
        passwordTV.setText(password);
        display = "Username: " + username;
        usernameTV.setText(username);


    }

    public void initializeButtons(View parentView){
        makeChangesButton = parentView.findViewById(R.id.changeSettingsButton);
        saveChangesButton = parentView.findViewById(R.id.saveChangesButton);
        deleteAccountButton = parentView.findViewById(R.id.deleteAccountButton);

        makeChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Change Settings");
                //flip between layouts
                defaultSettingsView.setVisibility(View.GONE);
                editSettingsView.setVisibility(View.VISIBLE);
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the thing to save changes and reinitialize settings
                Log.d(TAG, "onClick: saveSettings");

                //add information
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userID);

                //get input
                Log.d(TAG, "onClick: Get Input");

                String tempFName = firstNameE.getText().toString();
                String tempLName =  lastNameE.getText().toString();
                String tempUser =  usernameE.getText().toString();
                String tempEmail =  emailE.getText().toString();
                String tempPassword = passwordE.getText().toString();


                //find the non empty fields
                if(tempFName.isEmpty())
                    params.put("firstName", firstName);
                else {
                    firstName = tempFName;
                    params.put("firstName", firstName);
                }
                if(tempLName.isEmpty())
                    params.put("lastName", lastName);
                else {
                    lastName = tempLName;
                    params.put("lastName", tempLName);
                }
                if(tempUser.isEmpty())
                    params.put("login", username);
                else {
                    username= tempUser;
                    params.put("login", tempUser);
                }
                if(tempEmail.isEmpty())
                    params.put("email", email);
                else {
                    email = tempEmail;
                    params.put("email", tempEmail);
                }
                if(tempPassword.isEmpty())
                    params.put("password", password);
                else {
                    password = tempPassword;
                    params.put("password", tempPassword);
                }

                Log.d(TAG, "onClick: Get Information: " + firstName + password + email + username + password);
                parameter = new JSONObject(params);

                //post changes
                Log.d(TAG, "onClick: Start Post");
                postChanges();

                //reinitialize settings
                initializeSettings(parentView);
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete the account and start main activity
                Log.d(TAG, "onClick: Delete Account");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                //make a toast that the account was deleted
                startActivity(intent);
            }
        });
    }

    public void initializeEditText(View parentView){
        firstNameE = parentView.findViewById(R.id.settingsEditFirstName);
        lastNameE = parentView.findViewById(R.id.settingsLastName);
        usernameE = parentView.findViewById(R.id.settingsEditUsername);
        emailE = parentView.findViewById(R.id.settingsEditEmail);
        passwordE = parentView.findViewById(R.id.settingsEditPassword);
    }

    private void connectToHTTP(){
        client = new OkHttpClient();
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Login";
    }
    
    public void postChanges(){
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), parameter.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e(TAG, "onFailure: Failed to post", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                try {
                    String json = response.body().string();
                    JSONObject jsonObject  = new JSONObject(json);

                    String error = jsonObject.getString("error");
                    Log.d(TAG, "onResponse: " + error);
                    //find error
                    if(!error.isEmpty()){
                        //make a toast
                        Toast toast = Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("firstName", firstName);
                        editor.putString("lastName", lastName);
                        editor.putString("email", email);
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.commit();

                        Log.d(TAG, "onResponse: Put in sharedPreferences!");

                        //toast!
                        Toast toast = Toast.makeText(getActivity(), "User Updated!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                catch (JSONException e){
                    Log.e(TAG, "onResponse: JSONObject failure", e);
                }

            }
        });

    }

}