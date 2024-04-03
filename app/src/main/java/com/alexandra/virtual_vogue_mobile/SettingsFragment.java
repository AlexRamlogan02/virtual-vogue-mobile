package com.alexandra.virtual_vogue_mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

class SettingsFragment extends Fragment {

    private TextView tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View parentView =  inflater.inflate(R.layout.fragment_settings, container, false);
        tv = requireView().findViewById(R.id.firstNameDisplay);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onFirstNameClicked(v);
                return false;
            }
        });

        return parentView;
    }

    public void onFirstNameClicked(View view) {
        tv.setVisibility(View.GONE);
    }
}