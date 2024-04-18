package com.alexandra.virtual_vogue_mobile;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class myOutfits extends Fragment {
    String TAG = "myOutfits";
    OkHttpClient client;
    TextView text;
    FloatingActionButton floatingActionButton;
    //camera
    Intent intent;
    String url;
    SharedPreferences sharedPreferences;
    ImageView imageView, imageView2;
    LinearLayout linearLayout;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: Create SignUp Fragment");

        View parentView =  inflater.inflate(R.layout.fragment_my_outfits, container, false);

        client = new OkHttpClient();
        text = parentView.findViewById(R.id.outfitsTitle);
        linearLayout = (LinearLayout) parentView.findViewById(R.id.imagesLayout);
        sharedPreferences = this.getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("user", null);
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Outfits/" + name;

        //imageView = (ImageView) parentView.findViewById(R.id.imageView);
        //imageView2 = (ImageView) parentView.findViewById(R.id.imageView2);
        fetchImages();



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

    public void fetchImages(){
        Request request = new Request.Builder().url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "BRUUUUH");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "BRUUUUH");
                String json = response.body().string();
                JSONObject jobj = null;

                try {
                    jobj = new JSONObject(json);

                    if (!jobj.getBoolean("success")){
                        imageView.setVisibility(View.GONE);
                        imageView2.setVisibility(View.GONE);
                    }
                    else {
                        JSONArray jsonArray = jobj.getJSONArray("outfits");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject outfit = jsonArray.getJSONObject(i);
                            String clothesUrl = outfit.getString("shirtURL");
                            String pantsUrl = outfit.getString("pantsURL");
                            String name = outfit.getString("outfitName");

                            URL curl = new URL(clothesUrl);
                            URL pants = new URL(pantsUrl);
                            Bitmap bmp = BitmapFactory.decodeStream(curl.openConnection().getInputStream());
                            Bitmap bmp2 = BitmapFactory.decodeStream(pants.openConnection().getInputStream());

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.lemands);
                                    ImageView imgView = new ImageView(getActivity());
                                    ImageView imgView2 = new ImageView(getActivity());
                                    TextView textView = new TextView(getContext());

                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(650, 650);
                                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(650, 650);
                                    lp.setMargins(-225,0,0,0);
                                    lp2.setMargins(-225,0,0,180);

                                    imgView.setLayoutParams(lp);
                                    imgView2.setLayoutParams(lp2);
                                    imgView.setPadding(-25,15,-25,15);
                                    imgView2.setPadding(-50,15,-50,-15);
                                    imgView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.container_card));
                                    imgView2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.container_card));

                                    linearLayout.addView(textView);
                                    linearLayout.addView(imgView);
                                    linearLayout.addView(imgView2);


                                    textView.setText(name);
                                    textView.setTextColor(getResources().getColor(R.color.black));
                                    textView.setTextSize(50);
                                    textView.setX(820);
                                    textView.setY(600);
                                    textView.setTypeface(typeface);

                                    imgView.setImageBitmap(bmp);
                                    imgView2.setImageBitmap(bmp2);
                                }
                            });
                        }
                    }

                    //Glide.with(getActivity()).load(clothesUrl).into(imageView);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        });



    }

}