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

public class loginFragment extends Fragment {
    String TAG = "loginFragment";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Called");

        View parentView =  inflater.inflate(R.layout.fragment_login, container, false);

        //place listeners on the edit boxes!

        return parentView;
    }

    private void onButtonClick(View view){
        Log.d(TAG, "onButtonClick: Started");

        //send login request!
    }

}