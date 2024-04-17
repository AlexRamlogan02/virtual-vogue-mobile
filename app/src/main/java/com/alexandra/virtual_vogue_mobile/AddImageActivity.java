package com.alexandra.virtual_vogue_mobile;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class AddImageActivity extends AppCompatActivity {

    Button mOpenLibraryButton;
    Button mOpenCameraButton;
    Button mSubmitButton;
    Button mCancelButton;
    RadioGroup mRadioGroup;
    RadioButton top;
    RadioButton bottoms;
    RadioButton dress;
    ImageView imageView;
    int selectedClothing;

    Toast toast;

    String TAG = "AddImageActivity";
    Uri outOutFileUri;

    //activity launcher

    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri o) {
                    if (o == null){
                        //Toast.makeText(AddImageActivity.this, "No Image Selected!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onActivityResult: No  Image Selected");
                    }else{
                        Glide.with(getApplicationContext()).load(o).into(imageView);
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        mOpenLibraryButton = findViewById(R.id.OpenLibrary);
        mOpenCameraButton = findViewById(R.id.takePictureButton);
        mSubmitButton = findViewById(R.id.submitButton);
        mCancelButton = findViewById(R.id.cancelButton);

        //set click listeners for buttons
        Log.d(TAG, "onCreate: Grabbed Buttons");

        mOpenCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open the camera
                Log.d(TAG, "onClick: open camera");
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivity(openCamera);
                } catch (Exception e) {
                    Log.e(TAG, "onClick: Error Opening Camera", e);
                }
            }
        });

        mOpenLibraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open photo library
                Log.d(TAG, "onClick: Open Library");

                imageView = findViewById(R.id.imageView);
                launcher.launch(new PickVisualMediaRequest.Builder().
                        setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
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

        //Radio Buttons

        Log.d(TAG, "onCreate: Initialize Radio Group");
        mRadioGroup = findViewById(R.id.clothingSelectionGroup);
        top = findViewById(R.id.shirtButton);
        bottoms = findViewById(R.id.pantsButton);
        dress = findViewById(R.id.dressButton);
        Log.d(TAG, "onCreate: Grabbed RadioGroup and buttons");


        //set click listeners
        for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
            RadioButton radio = (RadioButton) mRadioGroup.getChildAt(i);
            radio.setOnClickListener(this::onClothingSelected);
        }

    }
}
    public void onClothingSelected(View view) {
        selectedClothing = R.string.top;
        if (view.getId() == R.id.shirtButton) {
            selectedClothing = R.string.top;
        } else if (view.getId() == R.id.pantsButton) {
            selectedClothing = R.string.bottoms;
        } else if (view.getId() == R.id.dressButton) {
            selectedClothing = R.string.dress;
        }
    }



}