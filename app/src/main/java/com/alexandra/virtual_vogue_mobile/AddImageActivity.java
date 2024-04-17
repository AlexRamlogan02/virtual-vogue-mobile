package com.alexandra.virtual_vogue_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kotlin.UByteArray;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    String url;
    Map <String, String> params;
    OkHttpClient client;
    String userID;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    int selectedClothing;
    String TAG = "AddImageActivity";

    boolean selectedType;
    boolean imageLoaded;
    private static final int pic_id = 123;

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    //activity launcher for photo library
    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri o) {
                    if (o == null) {
                        //Toast.makeText(AddImageActivity.this, "No Image Selected!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onActivityResult: No  Image Selected");
                    } else {
                        Glide.with(getApplicationContext()).load(o).into(imageView);
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        //findUserID
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        userID = sharedPreferences.getString("user", "NULL");


        //prepare httpclient
        params = new HashMap<String, String>();
        client= new OkHttpClient();
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Upload/" + userID;
        Log.d(TAG, "onCreate: url " + url);

        mOpenLibraryButton = findViewById(R.id.OpenLibrary);
        mOpenCameraButton = findViewById(R.id.takePictureButton);
        mSubmitButton = findViewById(R.id.submitButton);
        mCancelButton = findViewById(R.id.cancelButton);

        //image view
        imageView = findViewById(R.id.imageView);
        //set click listeners for buttons
        Log.d(TAG, "onCreate: Grabbed Buttons");

        //submit button disabled
        mSubmitButton.setEnabled(false);
        mSubmitButton.setBackgroundColor(getResources().getColor(R.color.black_overlay));

        mOpenCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open the camera
                Log.d(TAG, "onClick: open camera");
                Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(openCamera, pic_id);
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
                post();

            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //return to main menu
                Log.d(TAG, "onClick: Return");
                finish();
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

    public void onClothingSelected(View view) {
        selectedClothing = R.string.top;
        if (view.getId() == R.id.shirtButton) {
            selectedClothing = R.string.top;
        } else if (view.getId() == R.id.pantsButton) {
            selectedClothing = R.string.bottoms;
        } else if (view.getId() == R.id.dressButton) {
            selectedClothing = R.string.dress;
        }
        selectedType = true;
        if (canEnable()){
            mSubmitButton.setEnabled(true);
            mSubmitButton.setBackgroundColor(getResources().getColor(R.color.reseda_green));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: Check pic_id");
        if (requestCode == pic_id) {
            imageLoaded = true;
            if (canEnable()){
                mSubmitButton.setEnabled(true);
                mSubmitButton.setBackgroundColor(getResources().getColor(R.color.reseda_green));
            }

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            try {
                //compress the photo as a PNG to post later
                photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
                Log.d(TAG, "onActivityResult: Successful Compression");
            }catch (Exception e){
                Toast.makeText(AddImageActivity.this, "Error saving image! Please try again", Toast.LENGTH_SHORT);
            }

        }
        Log.d(TAG, "onActivityResult: ");
    }
}

    private boolean canEnable(){
        return(selectedType && imageLoaded);
    }

    private void post(){
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", userID)
                .addFormDataPart("image", "filename.png", RequestBody.create(MediaType.parse("image/*jpg"), out.toByteArray()))
                .addFormDataPart("tag", getResources().getString(selectedClothing))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    String json = response.body().string();
                    JSONObject object = new JSONObject(json);

                    Log.d(TAG, "onResponse: json " + json);

                    boolean success = object.getBoolean("success");

                    Log.d(TAG, "onResponse: " + String.valueOf(success));
                    if(success){
                        Log.d(TAG, "onResponse: Sucessful post");
                        finish();
                    }
                    else{
                        Log.d(TAG, "onResponse: Unsuccessful post");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
