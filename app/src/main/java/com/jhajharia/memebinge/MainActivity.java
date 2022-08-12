package com.jhajharia.memebinge;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String currImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMeme();
    }

    private void loadMeme() {
        ProgressBar loadSpinner = findViewById(R.id.progressBar);
        loadSpinner.setVisibility(View.VISIBLE);
        ImageView meme_image = (ImageView) findViewById(R.id.memeImage);

        String url = "https://meme-api.herokuapp.com/gimme";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String meme_url= null;
                        try {
                            meme_url = response.getString("url");
                            currImageUrl = meme_url;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Glide.with(MainActivity.this).load(meme_url).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                loadSpinner.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                loadSpinner.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(meme_image);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void next(View view) {
        loadMeme();
    }

    public void share(View view) {
        String extra = "Ya meme dekh : ";
        String guide = "Share this image using ";
        Intent shareIt = new Intent(Intent.ACTION_SEND);
        shareIt.setType("text/plain");
        shareIt.putExtra(shareIt.EXTRA_TEXT, extra + currImageUrl);
        Intent chooser = Intent.createChooser(shareIt, guide);
        startActivity(chooser);
    }
}