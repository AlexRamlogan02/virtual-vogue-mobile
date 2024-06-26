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
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.TypedValue;
import android.view.Gravity;
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
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class myOutfits extends Fragment {

    public static class Outfits{
        String name;
        public Outfits(String name){
            this.name = name;
        }
    }
    String TAG = "myOutfits";
    OkHttpClient client;
    TextView text;
    FloatingActionButton floatingActionButton;
    //camera
    Intent intent;
    String url, url2;
    SharedPreferences sharedPreferences;
    ImageView imageView, imageView2;
    LinearLayout linearLayout;
    Map<String, String> params;
    Map<String, Outfits> allOutfits;
    String name;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: Create SignUp Fragment");

        View parentView =  inflater.inflate(R.layout.fragment_my_outfits, container, false);
        params = new HashMap<String, String>();
        allOutfits = new HashMap<String, Outfits>();
        client = new OkHttpClient();
        text = parentView.findViewById(R.id.outfitsTitleS);
        text.setVisibility(View.GONE);
        linearLayout = (LinearLayout) parentView.findViewById(R.id.imagesLayout);
        sharedPreferences = this.getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("user", null);
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
                        text.setVisibility(View.VISIBLE);
                    }
                    else {
                        JSONArray jsonArray = jobj.getJSONArray("outfits");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject outfit = jsonArray.getJSONObject(i);
                            String clothesUrl = outfit.getString("shirtURL");
                            String pantsUrl = outfit.getString("pantsURL");
                            String name = outfit.getString("outfitName");
                            Outfits fit = new Outfits(name);
                            allOutfits.put(name, fit);

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
                                    LinearLayout innerLayout = new LinearLayout(getContext());
                                    LinearLayout.LayoutParams linParams = new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT);
                                    linParams.setMargins(45, 25, 45, 25);
                                    innerLayout.setLayoutParams(linParams);

                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    );
                                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    );
                                    innerLayout.setOrientation(LinearLayout.VERTICAL);
                                    innerLayout.setGravity(Gravity.CENTER);

                                    imgView.setLayoutParams(lp);
                                    imgView2.setLayoutParams(lp2);
                                    //imgView.setPadding(25,15,25,15);
                                    //imgView2.setPadding(50,15,50,15);
                                    innerLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.container_card));

                                    textView.setText(name);
                                    textView.setTextColor(getResources().getColor(R.color.black));
                                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

                                    textView.setTypeface(typeface);
                                    textView.setGravity(Gravity.CENTER);

                                    imgView.setImageBitmap(bmp);
                                    imgView2.setImageBitmap(bmp2);
                                    innerLayout.addView(textView);
                                    innerLayout.addView(imgView);
                                    innerLayout.addView(imgView2);
                                    linearLayout.addView(innerLayout);
                                    setOutfitDelete(innerLayout, name);

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

    public void setOutfitDelete(LinearLayout innerLayout, String key){

        innerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Outfits currentOutfit = allOutfits.get(key);
                confirmDelete(currentOutfit.name);
            }
        });
    }

    private void confirmDelete(String outfitName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to delete this outfit?");
        builder.setTitle("Confirm Account Deletion");
        url2 = "https://virtvogue-af76e325d3c9.herokuapp.com/api/Outfits/" + name + "/" + outfitName;

        Log.d(TAG, "confirmDelete: Settings Buttons");
        //positive button
        builder.setPositiveButton("Yes, delete this outfit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d(TAG, "onClick: Continue Cancellation");
                //make parameters
                params = new HashMap<String, String>();
                params.put("outfitName",outfitName);
                params.put("userId", name);

                JSONObject parameter = new JSONObject(params);

                Log.d(TAG, "onClick: Start Delete");
                //call delete
                deleteOutfit();
                //return to main
                Intent intent = new Intent(getActivity(), landingPage.class);
                //make a toast that the account was deleted
                startActivity(intent);
            }
        });


        //negative
        builder.setNegativeButton("No, keep my outfit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancel
            }
        });

        Log.d(TAG, "confirmDelete: Showing Dialog");

        AlertDialog dialog = builder.create();
        dialog.show();
        Log.d(TAG, "confirmDelete: dialog showed");
    }

    public void deleteOutfit(){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject parameter = new JSONObject(params);
        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(url2)
                .delete(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Intent intent = new Intent(getActivity(), landingPage.class);
                startActivity(intent);
            }
        });

    }

}