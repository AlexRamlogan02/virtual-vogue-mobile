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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class outfitCreationFragment extends Fragment {

    public static class Clothes{

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

    Map<Integer, Clothes> Closet;

    SharedPreferences sharedPreferences;
    OkHttpClient client;
    String url, name;
    ImageView imageView;
    String TAG = "createOutfits";
    TextView text;
    GridLayout displayGrid;
    View gridRoot;
    int COL;
    int ROW;

    ViewGroup root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View parentView =  inflater.inflate(R.layout.fragment_outfit_creation, container, false);
        root = parentView.findViewById(R.id.frameForGrid);

        client = new OkHttpClient();
        imageView = parentView.findViewById(R.id.imageViewShirt);
        //text = parentView.findViewById(R.id.blank);
        sharedPreferences = this.getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("user", null);
        url = "https://virtvogue-af76e325d3c9.herokuapp.com/api/images/" + name;
        Closet = new HashMap<Integer, Clothes>();

        fetchClothes();
        return parentView;
    }

    public void fetchClothes()
    {
        Request request = new Request.Builder().url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                JSONObject jobj = null;
                JSONObject imageObj = null;
                String imageURL;
                String label;

                try {
                    jobj = new JSONObject(json);
                    JSONArray jsonArray = jobj.getJSONArray("images");

                    for (int i = 0; i < jsonArray.length(); i++) {


                        imageObj = jsonArray.getJSONObject(i);

                        label = imageObj.getString("tag");
                        URL clothesURL = new URL(imageObj.getString("url"));
                        Bitmap bitmap = BitmapFactory.decodeStream(clothesURL.openConnection().getInputStream());


                        //add all to closet
                        Clothes clothing = new Clothes(clothesURL, label, i);
                        Closet.put(i, clothing);
                        Log.d(TAG, "onResponse: " + Closet.get(i).label + i);

                    }
                    Log.d(TAG, "onCreateView: start display clothes" + Closet.size());
                    displayClothes();
                } catch (JSONException e){
                    throw new RuntimeException(e);
                }
            }
        });


    }

    public void displayClothes(){
        Log.d(TAG, "displayClothes: " + Closet.size());
        //initialize the grid layout
        ROW = Closet.size();
        COL = 1;
        displayGrid = new GridLayout(getActivity());
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        displayGrid.setLayoutParams(params);
        displayGrid.setColumnCount(COL);
        displayGrid.setRowCount(ROW);


        for (int i = 0; i < ROW; i++) {

            Clothes clothing = Closet.get(i);
            Log.d(TAG, "displayClothes: " + clothing.label);

            //create a child
            LinearLayout cardView = new LinearLayout(getActivity());
            cardView.setElevation(5);
            cardView.setBackground(getResources().getDrawable(R.drawable.container_card));
            ViewGroup.LayoutParams cardParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardView.setLayoutParams(cardParams);

            Log.d(TAG, "displayClothes: set linear layout");
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
            cardView.addView(label);

            Log.d(TAG, "displayClothes: set text" + clothing.label);

            //add the imageView
            ImageView currentImage = new ImageView(getActivity());
            ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(
                    100, 200
            );
            currentImage.setLayoutParams(imageParams);
            currentImage.setImageBitmap(clothing.image);
            currentImage.setForegroundGravity(Gravity.CENTER);
            cardView.addView(currentImage);

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
            deleteButton.setBackgroundColor(getResources().getColor(R.color.blush));
            deleteButton.setForegroundGravity(Gravity.CENTER);
            deleteButton.setPadding(10, 10 , 10 , 10);
            setDeleteButton(deleteButton, i);
            cardView.addView(deleteButton);

            Log.d(TAG, "displayClothes: add button");

            GridLayout.Spec rowSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1);
            GridLayout.Spec colSpan = GridLayout.spec(GridLayout.UNDEFINED, 1, 1);

            GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams(rowSpan, colSpan);
            displayGrid.addView(cardView, gridParams);
            Log.d(TAG, "displayClothes: Add Linear Layout");
        }

        Log.d(TAG, "displayClothes: Try to add to root");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                root.addView(displayGrid);
            }
        });

        Log.d(TAG, "displayClothes: finish");

    }

    public void setDeleteButton(final Button button, int child){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Delete child" + child);
            }
        });

    }

}