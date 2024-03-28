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

public class signUpFragment extends Fragment {

    String TAG = "signupFragment";
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: Create SignUp Fragment");

        View parentView =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        //place listeners on the edit boxes!

        return parentView;
    }
}