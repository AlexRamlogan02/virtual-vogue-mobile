package com.alexandra.virtual_vogue_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class outfitCreationFragment extends Fragment {

    public static class Clothes {

        String label;
        URL imageUrl;
        int mapIndex;
        Bitmap image;

        public Clothes(URL imageUrl, String label, int mapIndex) throws IOException {
            this.imageUrl = imageUrl;
            this.label = label;
            this.mapIndex = mapIndex;
            image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
        }
    }

    public static class Outfit{
        String pantsID;
        String shirtID;
        String dressID;
        public Outfit(){
            pantsID = null;
            shirtID = null;
            dressID = null;
        }

        public void addShirt(String shirtID){
            this.shirtID = shirtID;
            if(dressID!=null){
                dressID = null;
            }
        }

        public void addPants(String pantsID){
            this.pantsID = pantsID;
            if(dressID!=null){
                dressID = null;
            }
        }

        public void addDress(String dressID){
            this.dressID = dressID;
            if(shirtID != null || pantsID != null){
                shirtID = null;
                pantsID = null;
            }
        }
    }

    Map<Integer, Clothes> Closet;
    Outfit outfit;
    SharedPreferences sharedPreferences;
    OkHttpClient client;
    String url, name;
    ImageView imageView;
    String TAG = "createOutfits";
    int COL;
    int ROW;
    ViewGroup root;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View parentView = inflater.inflate(R.layout.fragment_outfit_creation, container, false);
        root = (LinearLayout) parentView.findViewById(R.id.frameForGrid);

        client = new OkHttpClient();
        imageView = parentView.findViewById(R.id.imageViewShirt);
        //text = parentView.findViewById(R.id.blank);
        sharedPreferences = this.getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("user", null);
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/images/" + name;
        Closet = new HashMap<Integer, Clothes>();
        outfit = new Outfit();

        fetchClothes();

        return parentView;
    }

    public void fetchClothes() {

        RequestBody body = RequestBody.create(JSON, name);
        Request request = new Request.Builder().url(url).addHeader("Accept", "application/json")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: Failed", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                JSONObject jobj = null;
                JSONObject imageObj = null;
                String label;

                try {
                    jobj = new JSONObject(json);
                    JSONArray jsonArray = jobj.getJSONArray("images");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        imageObj = jsonArray.getJSONObject(i);

                        label = imageObj.getString("tag");
                        URL clothesURL = new URL(imageObj.getString("url"));
                        //add all to closet
                        Clothes clothing = new Clothes(clothesURL, label, i);
                        Closet.put(i, clothing);
                        Log.d(TAG, "onResponse: " + Closet.get(i).label + i);
                    }
                    Log.d(TAG, "onResponse: finish" + String.valueOf(call.isCanceled()));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                Log.d(TAG, "onCreateView: Finish fetch");
                            try {
                                Log.d(TAG, "onCreateView: Display Cothes" + Closet.size());
                                displayClothes();
                            } catch (Exception e) {
                                Log.d(TAG, "onResponse: Didn't work :/");
                                Log.e(TAG, "onResponse: ", e);
                            }
                        }
                    });


                } catch (Exception e) {
                    Log.e(TAG, "onResponse: ", e);
                }
            }
        });

        Log.d(TAG, "onResponse: end");
    }

    public void displayClothes(){
        Log.d(TAG, "displayClothes: " + Closet.size());
        //initialize the grid layout
        GridLayout grid = new GridLayout(getContext());
        ViewGroup.LayoutParams gridViewparams= new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        grid.setLayoutParams(gridViewparams);
        grid.setForegroundGravity(Gravity.CENTER);
        grid.setBackgroundColor(getResources().getColor(R.color.white));
        grid.setPadding(6, 6, 6, 6);
        ROW =  (Closet.size() / 3 ) + (Closet.size() % 3);
        Log.d(TAG, "displayClothes: " + Closet.size() % 3);
        COL = 3;
        grid.setRowCount(ROW);
        grid.setColumnCount(COL);
        int curRow =0 ;
        int curCol = 0;

        for (int i = 0; i < Closet.size(); i++) {
            Clothes clothing = Closet.get(i);
            LinearLayout linearLayout = new LinearLayout(getContext());
            ViewGroup.LayoutParams linearParams= new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            if (curCol == 3){
                curCol = 0;
                curRow++;
            }


            linearLayout.setLayoutParams(linearParams);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setForegroundGravity(Gravity.CENTER);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(30, 30 , 30, 30);


            //add the label, imageView, and delete button
            TextView label = new TextView(getActivity());
            ViewGroup.LayoutParams textParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            label.setLayoutParams(textParams);

            //add the text view
            label.setText(clothing.label);
            label.setTextColor(getResources().getColor(R.color.black));
            label.setTypeface(getResources().getFont(R.font.lemands));
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            label.setGravity(Gravity.CENTER);
            linearLayout.addView(label);

            Log.d(TAG, "displayClothes: set text" + clothing.label);

            //add the imageView
            ImageView currentImage = new ImageView(getActivity());
            ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 500
            );

            currentImage.setLayoutParams(imageParams);
            currentImage.setImageBitmap(clothing.image);
            currentImage.setForegroundGravity(Gravity.CENTER);
            setImageClick(currentImage, clothing, i);
            linearLayout.addView(currentImage);

            Log.d(TAG, "displayClothes: add image");

            //add the button
            Button deleteButton = new Button(getActivity());
            ViewGroup.LayoutParams buttonParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            deleteButton.setLayoutParams(buttonParams);
            deleteButton.setText("Delete");
            deleteButton.setTextColor(getResources().getColor(R.color.white));
            deleteButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            deleteButton.setBackgroundColor(getResources().getColor(R.color.blush));
            deleteButton.setForegroundGravity(Gravity.CENTER);
            deleteButton.setPadding(8, 2 , 8 , 2);
            setDeleteButton(deleteButton, clothing);
            linearLayout.addView(deleteButton);

            Log.d(TAG, "displayClothes: add button");


            GridLayout.Spec row = GridLayout.spec(curRow);
            GridLayout.Spec col = GridLayout.spec(curCol,1);
            GridLayout.LayoutParams innerParam = new GridLayout.LayoutParams(
                    row, col
            );

            grid.addView(linearLayout, innerParam);
            curCol++;
            Log.d(TAG, "displayClothes: add to grid");
        }

        root.addView(grid);

    }

    public void setDeleteButton(final Button button, Clothes clothing){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Delete");
            }
        });
    }

    public void setImageClick(final ImageView imageView,  Clothes clothing, int key){
        String tag = clothing.label;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView img;

                if(tag.equals("Shirt")) {
                    img = getActivity().findViewById(R.id.imageViewShirt);
                } else if (tag.equals("Pants")) {
                    img = getActivity().findViewById(R.id.imageViewPants);
                } else{
                    ImageView shirt = getActivity().findViewById(R.id.imageViewShirt);
                    ImageView pants = getActivity().findViewById(R.id.imageViewPants);
                    if(shirt.getDrawable() != null || pants.getDrawable() != null){
                        shirt.setImageDrawable(null);
                        pants.setImageDrawable(null);
                    }
                    img = getActivity().findViewById(R.id.imageViewDress);
                    shirt.setVisibility(View.GONE);
                    pants.setVisibility(View.GONE);
                }
                img.setVisibility(View.VISIBLE);
                img.setImageBitmap(clothing.image);
            }
        });
    }

}