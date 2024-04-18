package com.alexandra.virtual_vogue_mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    boolean confirmation;
    Button makeChangesButton;
    Button saveChangesButton;
    Button deleteAccountButton;
    Button logOutButton;
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

    String tempFName = "";
    String tempLName = "";
    String tempUser = "";
    String tempEmail = "";
    String tempPassword = "";
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
        } catch (Exception e) {
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

    public void initializeSettings(View parentView) {
        defaultSettingsView = parentView.findViewById(R.id.defaultSettingsView);
        editSettingsView = parentView.findViewById(R.id.editSettingsLayout);

        //ensure that the correct layout is visible
        if (defaultSettingsView.getVisibility() == View.GONE) {
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
        lastNameTV.setText(display);
        display = "Email: " + email;
        emailTV.setText(display);
        //disguise the password
        StringBuilder tempPass = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            if (i < password.length() % 2)
                tempPass.append(password.charAt(i));
            tempPass.append("*");
        }
        display = "Password: " + tempPass.toString();
        passwordTV.setText(display);
        display = "Username: " + username;
        usernameTV.setText(display);


    }

    public void initializeButtons(View parentView) {
        makeChangesButton = parentView.findViewById(R.id.changeSettingsButton);
        saveChangesButton = parentView.findViewById(R.id.saveChangesButton);
        deleteAccountButton = parentView.findViewById(R.id.deleteAccountButton);
        logOutButton = parentView.findViewById(R.id.logoutButton);

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


                client = new OkHttpClient();
                url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/UpdateSettings";

                //add information
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userID);

                //get input
                Log.d(TAG, "onClick: Get Input");
                try {
                    tempFName = firstNameE.getText().toString();
                    if (tempFName.isEmpty()){
                        tempFName = firstName;
                    }
                    Log.d(TAG, "onClick: " + tempFName);
                }
                catch(Exception e){
                    tempFName = firstName;
                    Log.e(TAG, "onClick: Empty First Name", e);
                }

                try {
                    tempLName = lastNameE.getText().toString();
                    if (tempLName.isEmpty()){
                        tempLName = lastName;
                    }
                    Log.d(TAG, "onClick: " + tempLName);
                }
                catch(Exception e){
                    tempLName = lastName;
                    Log.e(TAG, "onClick: Empty Last Name", e);
                }
                try {
                    tempUser = usernameE.getText().toString();
                    if (tempUser.isEmpty()){
                        tempUser = username;
                    }
                    Log.d(TAG, "onClick: " + tempUser);
                }catch(Exception e){
                    tempUser = username;
                    Log.e(TAG, "onClick: Empty username", e);
                }

                try {
                    tempEmail = emailE.getText().toString();
                    if (tempEmail.isEmpty()){
                        tempEmail = email;
                    }
                    Log.d(TAG, "onClick: " + tempEmail);
                }
                catch (Exception e){
                    tempEmail = email;
                    Log.e(TAG, "onClick: Empty email", e);
                }
                try {
                    tempPassword = String.valueOf(passwordE.getText());
                    if (tempPassword.isEmpty()){
                        tempPassword = password;
                    }
                    Log.d(TAG, "onClick: " + tempEmail);
                }
                catch (Exception e){
                    tempPassword = password;
                    Log.e(TAG, "onClick: Empty password", e);
                }

                Log.d(TAG, "onClick: Add to HashMap");
                //find the non empty fields
                params.put("firstName", tempFName);
                params.put("lastName", tempLName);
                params.put("login", tempUser);
                params.put("email", tempEmail);
                params.put("password", tempPassword);

                Log.d(TAG, "onClick: Get Information: " + params);
                parameter = new JSONObject(params);

                //post changes
                Log.d(TAG, "onClick: Start Post");
                postChanges();
                Intent intent = new Intent(getActivity(), landingPage.class);
                startActivity(intent);
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete the account and start main activity
                Log.d(TAG, "onClick: Delete Account");
                client = new OkHttpClient();
                url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/DeleteUser/" + userID;

                //confirm deletion
                try {
                    confirmDelete();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void confirmDelete() throws Exception{

        Log.d(TAG, "confirmDelete: Building Dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to delete your account?");
        builder.setTitle("Confirm Account Deletion");

        Log.d(TAG, "confirmDelete: Settings Buttons");
        //positive button
        builder.setPositiveButton("Yes, delete my account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d(TAG, "onClick: Continue Cancellation");
                //make parameters
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userID);
                parameter = new JSONObject(params);

                Log.d(TAG, "onClick: Start Delete");
                //call delete
                postDelete();
                //return to main
                Intent intent = new Intent(getActivity(), MainActivity.class);
                //make a toast that the account was deleted
                startActivity(intent);
            }
        });


        //negative
        builder.setNegativeButton("No, keep my account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmation = false;
                //cancel
            }
        });

        Log.d(TAG, "confirmDelete: Showing Dialog");

        AlertDialog dialog = builder.create();
        dialog.show();
        Log.d(TAG, "confirmDelete: dialog showed");
    }

    public void initializeEditText(View parentView) {
        firstNameE = parentView.findViewById(R.id.settingsEditFirstName);
        lastNameE = parentView.findViewById(R.id.settingsEditLastName);
        usernameE = parentView.findViewById(R.id.settingsEditUsername);
        emailE = parentView.findViewById(R.id.settingsEditEmail);
        passwordE = parentView.findViewById(R.id.settingsEditPassword);
    }
    public void postChanges() {

        Log.d(TAG, "postChanges: Begin" + parameter);
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        Log.d(TAG, "postChanges: Created body and request");
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
                    JSONObject jobj = new JSONObject(json);
                    //error here?
                    String error = jobj.getString("error");
                    Log.d(TAG, "onResponse: " + error);
                    //find error
                    if (!error.isEmpty()) {
                        //make a toast
                        Log.d(TAG, "onResponse: Error" + error);
                        return;
                    } else {
                        //toast!
                        Log.d(TAG, "onResponse: Success");
                        sharedPreferences = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //add values to sharedPreferences

                        editor.putString("firstName", tempFName);
                        editor.putString("lastName", tempLName);
                        editor.putString("email", tempEmail);
                        editor.putString("username", tempUser);
                        editor.putString("password", tempPassword);
                        editor.commit();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: JSONObject failure", e);
                }

            }
        });

    }


    public void postDelete() {

        Log.d(TAG, "postChanges: Begin");
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        Log.d(TAG, "postChanges: Created body and request");
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
                    JSONObject jobj = new JSONObject(json);
                    //error here?
                    String success = jobj.getString("success");
                    //find error
                    if (success.equals("false")) {
                        //make a toast
                        Log.d(TAG, "onResponse: Error");
                        return;
                    } else {
                        //toast!
                        Log.d(TAG, "onResponse: Success");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: JSONObject failure", e);
                }
            }
        });

    }

}