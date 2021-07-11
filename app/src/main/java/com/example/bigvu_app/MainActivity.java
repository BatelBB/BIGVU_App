package com.example.bigvu_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> texts = new ArrayList<>();
    private ArrayList<String> videoUrls = new ArrayList<>();

    private static final String EXTRA_NAME = "com.example.bigvu_app.name";
    private static final String EXTRA_DESCRIPTION = "com.example.bigvu_app.description";
    private static final String EXTRA_TEXT = "com.example.bigvu_app.text";
    private static final String EXTRA_VIDEO = "com.example.bigvu_app.video";


    private RequestQueue mQueue;
    public CustomItemList customItemList;

    SearchView searchView;
    Handler mainHandler = new Handler();
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        new fetchData().start();


        searchView = (SearchView) findViewById(R.id.searchView);

        TextView textView = new TextView(this);
        textView.setTypeface(Typeface.DEFAULT_BOLD);


        ListView listView = (ListView) findViewById(R.id.list);
        mProgressBar = (ProgressBar) findViewById(R.id.indeterminateBar);

        customItemList = new CustomItemList(this, names, descriptions, imageUrls);
        listView.setAdapter(customItemList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                intent.putExtra(EXTRA_NAME, names.get(position));
                intent.putExtra(EXTRA_DESCRIPTION, descriptions.get(position));
                intent.putExtra(EXTRA_TEXT, texts.get(position));
                intent.putExtra(EXTRA_VIDEO, videoUrls.get(position));
                startActivity(intent);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                for (int i = 0; i < descriptions.size(); i++) {
//                    if (descriptions.get(i).contains(query)) {
//                        customItemList.getFilter().filter(query);
//                    } else {
//                        Toast.makeText(MainActivity.this, "No Match found", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                }
//                return false;
                customItemList.getFilter().filter(query);
                listView.setAdapter(customItemList);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customItemList.getFilter().filter(newText);
                return false;
            }
        });

    }


    class fetchData extends Thread {

        @Override
        public void run() {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBar = new ProgressBar(MainActivity.this);
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
//                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                }
            });

            String url = "https://bigvu-interviews-assets.s3.amazonaws.com/workshops.json";
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            processResponse(response);

                            customItemList.notifyDataSetChanged();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });


            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mProgressBar.isShown())
                        mProgressBar.setProgress(ProgressBar.GONE);
                }
            });

            mQueue.add(request);
        }

    }

    private void processResponse(JSONArray response) {
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject element = response.getJSONObject(i);

                names.add(element.getString("name"));
                imageUrls.add(element.getString("image"));
                descriptions.add(element.getString("description"));
                texts.add(element.getString("text"));
                videoUrls.add(element.getString("video"));

            }
        } catch (JSONException e) {
            Log.e("TAG", e.getMessage());

        }
    }
}

