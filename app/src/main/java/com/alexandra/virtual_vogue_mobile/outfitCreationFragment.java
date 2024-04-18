package com.alexandra.virtual_vogue_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    LinearLayout closetDisplay;
    String TAG = "createOutfits";
    TextView text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View parentView =  inflater.inflate(R.layout.fragment_outfit_creation, container, false);

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
                    }

                    for (int i = 0; i < Closet.size(); i++) {
                        Clothes clothing = Closet.get(i);
                        Log.d(TAG, "onResponse: " + clothing.label);
                    }

                } catch (JSONException e){
                    throw new RuntimeException(e);
                }
            }
        });

    }
}