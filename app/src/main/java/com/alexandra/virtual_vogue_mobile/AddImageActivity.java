package com.alexandra.virtual_vogue_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class AddImageActivity extends AppCompatActivity {

    Button mOpenLibraryButton;
    Button mOpenCameraButton;
    Button mSubmitButton;
    Button mCancelButton;
    RadioGroup mRadioGroup;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    //String name = sharedPreferences.getString("user", null);
    //String url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Upload/" + name;
    String TAG = "AddImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);


        imageView = findViewById(R.id.imageView);
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
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(openCamera, 1888);
                    Bitmap photo = (Bitmap) openCamera.getExtras().get("data");
                    imageView.setImageBitmap(photo);
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