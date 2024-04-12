package com.alexandra.virtual_vogue_mobile;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.alexandra.virtual_vogue_mobile.databinding.FragmentMyOutfitsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class myOutfits extends Fragment {
    String TAG = "myOutfits";
    TextView text;
    FloatingActionButton floatingActionButton;
    //camera
    Intent intent;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: Create SignUp Fragment");

        View parentView =  inflater.inflate(R.layout.fragment_my_outfits, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);




        floatingActionButton = parentView.findViewById(R.id.addToClosetButton);

        floatingActionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: Button Clicked");
                        intent = new Intent(getActivity(), AddImageActivity.class);

                        try {
                            startActivity(intent);
                        }
                        catch(Exception e){
                            Log.e(TAG, "onClick: Error opening Activity", e);
                        }
                    }
                }
        );


        return parentView;
    }

}