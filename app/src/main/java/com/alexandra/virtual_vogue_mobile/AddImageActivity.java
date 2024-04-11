package com.alexandra.virtual_vogue_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class AddImageActivity extends AppCompatActivity {

    Button mOpenLibraryButton;
    Button mOpenCameraButton;
    Button mSubmitButton;
    Button mCancelButton;
    RadioGroup mRadioGroup;

    String TAG = "AddImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);


        mOpenLibraryButton = findViewById(R.id.OpenLibrary);
        mOpenCameraButton = findViewById(R.id.takePictureButton);
        mSubmitButton = findViewById(R.id.submitButton);
        mCancelButton = findViewById(R.id.cancelButton);
        mRadioGroup = findViewById(R.id.clothingSelectionGroup);

        Log.d(TAG, "onCreate: Grabbed Buttons");

        //set click listeners for buttons

        Log.d(TAG, "onCreate: Initialize Radio Group");

        mOpenCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open the camera
                Log.d(TAG, "onClick: open camera");
                Intent openCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                try {
                    startActivity(openCamera);
                }
                catch(Exception e){
                    Log.e(TAG, "onClick: Error Opening Camera", e);
                }
            }
        });
        
        mOpenLibraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open photo library
                Log.d(TAG, "onClick: Open Library");
            }
        });
        
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submit the picture with an API
                Log.d(TAG, "onClick: Submit Photo");
            }
        });
        
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to main menu 
                Log.d(TAG, "onClick: Return");
            }
        });
    }

}